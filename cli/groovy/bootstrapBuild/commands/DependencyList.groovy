package bootstrapBuild.commands

import bootstrapBuild.utility.ParseDependents
import bootstrapBuild.utility.Profile
import bootstrapBuild.utility.Environment

class DependencyList {

   private static int buildListChildren(Environment props, Profile profile, String hatchname, List<String> dependencies) {

      File dependsfile = Environment.dependsfile(props,hatchname);
      List<String> children = ParseDependents.read(dependsfile,profile);

      for(String child:children) {
         if(!dependencies.contains(child)) {
            int retval = buildListOne(props,profile,child,dependencies);
            if(retval > 0) return retval;
         }
      }

      return 0
   }

   private static int buildListOne(Environment props, Profile profile, String hatchname, List<String> dependencies) {

      if(dependencies.contains(hatchname)) {
//      bootstrapLog $BOOTSTRAP_LOG_DEBUG "DEBUG: Found Listed ${hatchname}"
         return 0
      }

      if(Environment.haveHatchery(props)) {
         //  "Incubate" the product - i.e. search the hatchery for the "${hatchname}-bootstrap" ovum
         //      or attempt to find and load the ovum from a BOOTSTRAP_BUILD_REGISTRIES
         //      Note: that this breaks a chicken-and-egg problem. We always incubate the ovum
         //      because otherwise we wouldn't be able to check for the product's existence via
         //      "islaid-${hatchname}.sh" and "ishatched-${hatchname}.sh"
         if(!Incubate.isFetched(props,hatchname)) {
            if(Incubate.fetchItems(props,hatchname) != 0) {
               // TODO log error
               return 1;
            }
         }

      }

      buildListChildren(props,profile,hatchname,dependencies);

      if(!dependencies.contains(hatchname)) {
         dependencies.add(hatchname);
//      bootstrapLog $BOOTSTRAP_LOG_DEBUG "DEBUG: Found Listed ${hatchname}"
      }

      return 0;
   }

   public static List<String> buildList(Environment props, Profile profile, String... hatchnames) {

      List<String> dependencies = new ArrayList<String>();

      if(hatchnames == null || hatchnames.size() < 1) {
         // TODO
         //bootstrapLog $BOOTSTRAP_LOG_ERROR "ERROR: bootstrapbuildlist requires at least one argument."
         // Perhaps we may want to throw an Exception here?
         return dependencies;
      }

      for(String hatchname:hatchnames) {
         int retval = buildListOne(props,profile,hatchname,dependencies);
         if(retval > 0) {
            // throw an exception
         }
      }

      return dependencies;
   }

   public static void printList(Environment props, Profile profile, String... hatchnames) {
      //println("Dependencies:")
      for(String dep:buildList(props,profile,hatchnames)) {
         println(dep)
      }
   }

}
