//############################################################################
// bootstrap incubate helper functions
//
// BootstrapBuild - a powerful but easy tool to build and install modular
//   applications in a Unix environment that supports Bash shell.
//
// Copyright FogRising 2020
// Authors: Ryan Fogarty, Charles J. Remeikas
//
//############################################################################

package bootstrapBuild.commands

import bootstrapBuild.utility.Environment
import bootstrapBuild.utility.GitRegistry
import java.text.SimpleDateFormat

class Incubate {

   private static String hatchnamePath(Environment props, String hatchname) {
      return props.BOOTSTRAP_BUILD_HATCHERY + "/" + hatchname + "-bootstrap";
   }

   public static boolean isFetched(Environment props, String hatchname) {
      return Environment.haveHatchery(props) &&
             new File(hatchnamePath(props,hatchname)).exists();
   }

   private static int fetchItem(Environment props,
                                String hatchname,
                                boolean forceUpdate) {

      // First move any preexisting directory
      if (isFetched(props,hatchname)) {
         if(forceUpdate) {
            Date date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd-HHmm.ss");
            File moved = new File(hatchnamePath() + "-old-" + sdf.format(date));
            File f = new File(hatchnamePath());
            f.renameTo(moved);
         }
         else {
            return 1;
         }
      }

      boolean success = false;
      for(String registry:props.BOOTSTRAP_BUILD_REGISTRIES) {
         // TODO: registry creation should be more sophisticated than this...
         if(registry.startsWith("git=")) {
            registry = registry.substring(4);
            GitRegistry reg = new GitRegistry(registry);
            if(reg.load(hatchname,new File(props.BOOTSTRAP_BUILD_HATCHERY)) == 0) {
               success = true;
               break;
            }
         }
      }
      // Probably want to log an error if we did not succeed.
      if(!success) {
         // ...
//         if [ $reposFound -eq 0 ] ; then
//            bootstrapLog $BOOTSTRAP_LOG_ERROR "ERROR: ${hatchname}-bootstrap not found in any known registry"
//            return 1
//         elif [ ! -d "${hatchname}-bootstrap" ] ; then
//            bootstrapLog $BOOTSTRAP_LOG_ERROR "ERROR: unable to find ${hatchname}-bootstrap directory in hatchery: $(pwd)"
//            return 1
      }

      return 0;

   }

   public static int fetchItems(Environment props, String... args) {

      if (args == null || args.size() < 1) {
      //   bootstrapLog $BOOTSTRAP_LOG_ERROR "ERROR: _bootstrapincubate requires hatchname argument"
         return 1
      }

      //export hatchname=$1
      // Start by parsing out all of the parameters
      ArrayList<String> hatchnames = new ArrayList<String>(); // = args[0];
      boolean forceUpdate = false;
      for( String arg : args ) {
         if ( arg.equals("-f") ) {
            forceUpdate=true
         }
         else {
            hatchnames.add(arg);
         }
      }

      if(Environment.haveHatchery(props)) {
         // First move any preexisting directory
         for(String hatchname : hatchnames) {
            int ret = fetchItem(props,hatchname,forceUpdate);
            if(ret > 0) return ret;
         }
      }
      else {
         // Print log message
         //         bootstrapLog $BOOTSTRAP_LOG_ERROR "ERROR: BOOTSTRAP_BUILD_HATCHERY is inaccessible. Please correct and rerun."
//   else {
//      bootstrapLog $BOOTSTRAP_LOG_ERROR "ERROR: BOOTSTRAP_BUILD_HATCHERY env is not set or points to bad location"
//      return 1
//   }

      }

      return 0;

   }

} // class Incubate

