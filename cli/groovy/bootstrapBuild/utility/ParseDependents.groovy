package bootstrapBuild.utility

import java.util.logging.Logger

class ParseDependents {
   private static Logger logger = Logger.getLogger("")

   public static List<String> read(File dependsfile,Profile profile) {
      ArrayList<String> dependents = new ArrayList<String>();
      if(dependsfile.exists()) {
         dependsfile.eachLine { line ->
            if(line.trim()) {
               if(line.startsWith("//") || line.startsWith("#") || line.startsWith("%")) {
                  // do nothing
               }
               else {
                  String[] dependency = line.split(",");
                  if(dependency.size() == 2) {
                     // Check profile
                     if(profile.allows(dependency[1])) {
                        dependents.add(dependency[0]);
                     }
                  }
                  else if(dependency.size() == 1) {
                     dependents.add(line);
                  }
               }
            }
            // o.w. ignore empty line
         }
      }
      else {
         // Log error
         //logger.warning("$product does not appear to be installed on this system.")
      }
      return dependents;
   }
}
