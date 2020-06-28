package testBootstrapBuild

import bootstrapBuild.utility.BooleanExpressionParser
import bootstrapBuild.utility.BooleanExpressionParser.InvalidProfileExpression

class TestBooleanExpressionParser extends GroovyTestCase {
   void testSimpleIsExpression() {
      try {
         String simpleExpression = "is debug"
         BooleanExpressionParser.BooleanExpression expr = BooleanExpressionParser.build(simpleExpression);
         Set<String> profiles = new HashSet<String>();
         profiles.add("debug");
         BooleanExpressionParser.updateProfile(profiles);
         assertTrue(expr.evaluate());
      }
      catch(InvalidProfileExpression ipe) {
         fail(ipe.toString());
      }
   }

   void testSimpleAndExpression() {
      try {
         String simpleExpression = "is debug && is optimized"
         BooleanExpressionParser.BooleanExpression expr = BooleanExpressionParser.build(simpleExpression);
         Set<String> profiles = new HashSet<String>();
         profiles.add("debug");
         profiles.add("optimized");
         BooleanExpressionParser.updateProfile(profiles);
         assertTrue(expr.evaluate());

         profiles.remove("optimized");
         BooleanExpressionParser.updateProfile(profiles);
         assertFalse(expr.evaluate());
      }
      catch(InvalidProfileExpression ipe) {
         fail(ipe.toString());
      }
   }

   void testSimpleOrExpression() {
      try {
         String simpleExpression = "isNot debug || is optimized"
         BooleanExpressionParser.BooleanExpression expr = BooleanExpressionParser.build(simpleExpression);
         Set<String> profiles = new HashSet<String>();
         profiles.add("debug");
         profiles.add("optimized");
         BooleanExpressionParser.updateProfile(profiles);
         assertTrue(expr.evaluate());

         profiles.remove("optimized");
         BooleanExpressionParser.updateProfile(profiles);
         assertFalse(expr.evaluate());
      }
      catch(InvalidProfileExpression ipe) {
         fail(ipe.toString());
      }
   }

   void testParansExpression() {
      try {
         String simpleExpression = "isNot unknown && (isNot debug || is optimized)"
         BooleanExpressionParser.BooleanExpression expr = BooleanExpressionParser.build(simpleExpression);
         Set<String> profiles = new HashSet<String>();
         profiles.add("debug");
         profiles.add("optimized");
         BooleanExpressionParser.updateProfile(profiles);
         assertTrue(expr.evaluate());

         profiles.remove("optimized");
         BooleanExpressionParser.updateProfile(profiles);
         assertFalse(expr.evaluate());
      }
      catch(InvalidProfileExpression ipe) {
         fail(ipe.toString());
      }
   }

   void testUnnecessaryParansExpression() {
      try {
         String simpleExpression = "(((isNot unknown) && ((isNot debug || is optimized))))"
         BooleanExpressionParser.BooleanExpression expr = BooleanExpressionParser.build(simpleExpression);
         Set<String> profiles = new HashSet<String>();
         profiles.add("debug");
         profiles.add("optimized");
         BooleanExpressionParser.updateProfile(profiles);
         assertTrue(expr.evaluate());

         profiles.add("unknown");
         BooleanExpressionParser.updateProfile(profiles);
         assertFalse(expr.evaluate());
      }
      catch(InvalidProfileExpression ipe) {
         fail(ipe.toString());
      }
   }

   void testMalformedExpression1() {
      String simpleExpression = "((isNot unknown) && ((isNot debug || is optimized))))"
      String msg = shouldFail(InvalidProfileExpression, {
         BooleanExpressionParser.BooleanExpression expr = BooleanExpressionParser.build(simpleExpression);
      });
      assert(msg.startsWith("Malformed expression or predicate: "));
   }

   void testMalformedExpression2() {
      String simpleExpression = "isNot debug optimized"
      String msg = shouldFail(InvalidProfileExpression, {
         BooleanExpressionParser.BooleanExpression expr = BooleanExpressionParser.build(simpleExpression);
      });
      assert(msg.startsWith("Predicate \"isNot\" requires single argument but has 2"));
   }

   void testMalformedExpression3() {
      String simpleExpression = "has debug optimized foo"
      String msg = shouldFail(InvalidProfileExpression, {
         BooleanExpressionParser.BooleanExpression expr = BooleanExpressionParser.build(simpleExpression);
      });
      assert(msg.startsWith("Predicate \"has\" requires single argument but has 3"));
   }

   void testMalformedExpression4() {
      String simpleExpression = "((has debug) || (has optimized)"
      String msg = shouldFail(InvalidProfileExpression, {
         BooleanExpressionParser.BooleanExpression expr = BooleanExpressionParser.build(simpleExpression);
      });
      assert(msg.startsWith("Sub-expression missing closing parantheses:"));
   }

}
