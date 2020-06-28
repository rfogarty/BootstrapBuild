package bootstrapBuild.utility
import groovy.transform.InheritConstructors

class BooleanExpressionParser {

   // Private global state variable that can be used by
   // all BooleanExpression evaluation (without having
   // to pass the set of profiles manually ad nauseum).
   private static Set<String> gProfileOptions;

   // This should be called before evaluating any Profile
   // BooleanExpression
   public static void updateProfile(Set<String> profileOptions) {
      // Clone the passed options, so that a change requires
      // calling updateProfile (providing some semblance of
      // encapsulation)
      gProfileOptions = profileOptions.clone();
   }

   public static Set<String> getProfile() {
      // Return clone of profile options to
      // provide a bit of Draconian encapsulation.
      return gProfileOptions.clone();
   }

   // Abstract BooleanExpression type that can
   // represent complicated BooleanExpressions.
   public static interface BooleanExpression {
      boolean evaluate();
   }

   private static class AndExpression implements BooleanExpression {
      BooleanExpression left;
      BooleanExpression right;

      AndExpression(BooleanExpression first,BooleanExpression second) {
         left = first;
         right = second;
      }

      public boolean evaluate() {
         return left.evaluate() && right.evaluate();
      }
   }

   private static class OrExpression implements BooleanExpression {
      BooleanExpression left;
      BooleanExpression right;

      OrExpression(BooleanExpression first,BooleanExpression second) {
         left = first;
         right = second;
      }

      public boolean evaluate() {
         return left.evaluate() || right.evaluate();
      }
   }

   private static class HaveProfile implements BooleanExpression {
      String profile;

      HaveProfile(String aProfile) {
         profile = aProfile;
      }

      public boolean evaluate() {
         return gProfileOptions.contains(profile);
      }
   }

   private static class HaveNotProfile implements BooleanExpression {
      String profile;

      HaveNotProfile(String aProfile) {
         profile = aProfile;
      }

      public boolean evaluate() {
         return !gProfileOptions.contains(profile);
      }
   }

   // Exception thrown when a bad expression is passed to the below build method.
   @InheritConstructors
   public static class InvalidProfileExpression extends Exception {}

   //
   // Simple recursive descent parser to build profile expressions.
   //
   // Returns abstract BooleanExpression.
   //
   // Evaluates expressions of the form: has profile1 || (isNot profile2 && isNot profile3)
   //
   // Expressions support the following predicates:
   //   is <profile>
   //   has <profile>
   //   add <profile>
   //   isNot <profile>
   //   hasNot <profile>
   //
   // Expressions support the following conjunctives:
   //   <subexpression> && <subexpression>
   //   <subexpression> || <subexpression>
   //
   // Also note, conjunctives have equal precedence so are left-associative (same as Bash, C/C++,...)
   //
   // Finally expression evaluation supports parantheses to force associativity as needed.
   // Such as has profile1 || (isNot profile2 && isNot profile3)
   //
   public static BooleanExpression build(String expression) throws InvalidProfileExpression {
      return buildRecursive(expression,null);
   }

   // recursive implementation of the build function that performs deferred expression construction for
   // paranthetical evaluation (by building sub expression but deferring injection into expression AST by save and forward method).
   private static BooleanExpression buildRecursive(String expression,Map<String,BooleanExpression> deferredSub) throws InvalidProfileExpression {
      // First make sure to remove any whitespace
      expression = expression.trim();
      String lcexpression = expression.toLowerCase();

      if(expression.contains("(")) {
         int lastOpenParan = expression.lastIndexOf('(');
         String prefixExpression;
         if (lastOpenParan == 0) {
            prefixExpression = "";
         } else {
            prefixExpression = expression.substring(0, lastOpenParan);
         }
         String paranExpr = expression.substring(lastOpenParan + 1);
         if (!paranExpr.contains(")")) throw new InvalidProfileExpression("Sub-expression missing closing parantheses: ${paranExpr}");
         int firstCloseParan = paranExpr.indexOf(')');
         String subExpression = paranExpr.substring(0, firstCloseParan);

         String suffixExpression = paranExpr.substring(firstCloseParan + 1);
         if (paranExpr.size() == firstCloseParan + 1) {
            suffixExpression = "";
         } else {
            suffixExpression = paranExpr.substring(firstCloseParan + 1);
         }

         String randomKey = UUID.randomUUID().toString();
         if (deferredSub == null) {
            deferredSub = new HashMap<String, BooleanExpression>();
         }
         deferredSub.put(randomKey, buildRecursive(subExpression, deferredSub));
         String newExpression = "${prefixExpression} ${randomKey} ${suffixExpression}";
         // Now recurse
         return buildRecursive(newExpression, deferredSub);
      }
      // We are mimicking Bash (and other languages) parsing rules and thus
      // operators && and || have the sane precedence and also are
      // left associative.
      else if(expression.contains("&&") || expression.contains("||")) {
         int andIndex = expression.indexOf("&&");
         int orIndex = expression.indexOf("||");

         if(andIndex < 0 || (orIndex < andIndex && orIndex >=0)) {
            return new OrExpression(buildRecursive(expression.substring(0,orIndex),deferredSub),
                  buildRecursive(expression.substring(orIndex+2),deferredSub));
         }
         else {
            return new AndExpression(buildRecursive(expression.substring(0,andIndex),deferredSub),
                  buildRecursive(expression.substring(andIndex+2),deferredSub));
         }
      }
      // Note: is <profile>, has <profile>, add <profile>
      // are all synonyms and mean the same thing
      else if(lcexpression.startsWith("is ") ||
              lcexpression.startsWith("has ") ||
              lcexpression.startsWith("add ") ) {
         String[] tokens = expression.split(" ");
         if(tokens.size() != 2) throw new InvalidProfileExpression("Predicate \"${tokens[0]}\" requires single argument but has ${tokens.size() - 1} : ${expression}");
         return new HaveProfile(tokens[1]);
      }
      else if(lcexpression.startsWith("isnot ") ||
              lcexpression.startsWith("hasnot ")) {
         String[] tokens = expression.split(" ");
         if(tokens.size() != 2) throw new InvalidProfileExpression("Predicate \"${tokens[0]}\" requires single argument but has ${tokens.size() - 1} : ${expression}");
         return new HaveNotProfile(tokens[1]);
      }
      else if(deferredSub != null && deferredSub.containsKey(expression)) {
         return deferredSub.remove(expression);
      }
      else {
         throw new InvalidProfileExpression("Malformed expression or predicate: ${expression}")
      }
   }
}
