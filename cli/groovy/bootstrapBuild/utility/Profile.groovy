package bootstrapBuild.utility

import javafx.beans.binding.BooleanExpression

class Profile {

   class TargetedProfile {
      public String profile;
      public List<String> targets;

      TargetedProfile(String aProfile,String targetList) {
         profile = aProfile;
         targets = new ArrayList<String>();
         addTargets(targetList,targets);
      }

      static void addTargets(String targetList,List<String> targetlist) {
         String[] ts = targetList.split(",");
         for(String target:ts) {
            targetlist.add(target);
         }
      }
   }
   List<TargetedProfile> blackProfileList;
   List<TargetedProfile> whiteProfileList;
   List<String> globalProfileList;

   private void parseProfileArgument(String profileArg) {
      // Algorithm, first split by ")," to separate any
      // whitelist or blacklist targeted profiles.
      String[] incompleteProfilesSplit = s.split("\\),");
      for(String incompleteProfileSplit:incompleteProfilesSplit) {
         if(incompleteProfileSplit.contains("(")) {
            // Found targeted token
            String[] profilesAndTargets = incompleteProfileSplit.split("\\(");
            assert(profilesAndTargets.size() == 2);
            if(profilesAndTargets[0].contains(",")) {
               // profile is actually still a construction of multiple global profiles and the targeted profile
               String[] profileNames = profilesAndTargets[0].split(",");
               for(int i = 1; i < profileNames.size();++i) {
                  if(profileNames[i-1].startsWith("-") ||
                     profileNames[i-1].startsWith("+")) {
                     // TODO throw exception? Log error?
                  }
                  else {
                     globalProfileList.add(profileNames[i-1]);
                  }
               }
               String targetedProfilename = profileNames[profileNames.size()-1];
               if(targetedProfilename.startsWith("-")) {
                  targetedProfilename = targetedProfilename.substring(1);
                  blackProfileList.add(new TargetedProfile(targetedProfilename,profilesAndTargets[1]));
               }
               else if(targetedProfilename.startsWith("+")) {
                  targetedProfilename = targetedProfilename.substring(1);
                  whiteProfileList.add(new TargetedProfile(targetedProfilename,profilesAndTargets[1]));
               }
               else {
                  // TODO: throw exception? Log error?
               }
            }
         }
         else {
            String[] profileNames = incompleteProfileSplit.split(",");
            for(String profileName:profileNames) {
               if(profileName.startsWith("-") ||
                  profileName.startsWith("+")) {
                  // TODO throw exception?
               }
               else {
                  globalProfileList.add(profileName);
               }
            }
         }
      }
   }

   public String[] processCommandline(String[] options) {

      List<String> unprocessedOptions = new ArrayList<String>();

      for(int i = 0;i < options.size();++i) {
         String option = options[i];
         option = option.trim();
         if(option.startsWith("--profile=")) {
            String profileArg = option.substring("--profile=".size());
            parseProfileArgument(profileArg);
         }
         else if(option.equals("--profile")) {
            ++i;
            if(i < options.size()) {
               String profileArg = options[i];
               parseProfileArgument(profileArg);
            }
            else {
               // TODO: Log error?
            }
         }
         else {
            unprocessedOptions.add(option);
         }
      }

      return unprocessedOptions.toArray(new String[unprocessedOptions.size()]);
   }

   boolean allows(String profileExpression,String forhatchname) {
      // Note: profiles to not need to be in any particular order...
      Set<String> relevantProfiles = new HashSet<String>();
      // Start by adding global list
      for(String p:globalProfileList) {
         relevantProfiles.add(p);
      }
      // Next add any whitelist targeted profiles
      for(TargetedProfile tp:whiteProfileList) {
         if(tp.targets.contains(forhatchname)) {
            relevantProfiles.add(tp.profile);
         }
      }
      // Finally, exclude and blacklist targeted profiles
      for(TargetedProfile tp:blackProfileList) {
         if(tp.targets.contains(forhatchname)) {
            relevantProfiles.remove(tp.profile);
         }
      }

      // Now update profiles and evaluate expressions.
      BooleanExpressionParser.updateProfile(relevantProfiles);
      BooleanExpressionParser.BooleanExpression expr = BooleanExpressionParser.build(profileExpression);
      return expr.evaluate();
   }
}
