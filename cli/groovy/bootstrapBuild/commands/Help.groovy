package bootstrapBuild.commands

class Help {

   static private final String bold = "\033[0;1m"
   static private final String normal = "\033[0;0m"

   private static GString incubateHelp() {
      return """\
Call with:
   >> ${bold}bootstrap incubate|stage${normal} <product>
   Note: incubate and stage are synonyms.
"""
   }

   private static GString islaidHelp() {
      return """\
Call with:
   >> ${bold}bootstrap islaid|isdownloaded${normal} <product>
   Note: islaid and isdownloaded are synonyms.
"""
   }

   private static GString layHelp() {
      return """\
Call with:
   >> ${bold}bootstrap lay|download${normal} <product>
   Note: lay and download are synonyms.
"""
   }

   private static GString ishatchedHelp() {
      return """\
   Call with:
   >> ${bold}bootstrap inhatched|isinstalled${normal} <product> [--profile=<P1,P2,P3>]
   Note: ishatched and isinstalled are synonyms.
"""
   }

   private static GString hatchHelp() {
      return """\
   Call with:
   >> ${bold}bootstrap hatch|install${normal} <product> [-b] [-bb] [--profile=<P1,P2,P3>]
   -b            - to force a build
   -bb           - to force a recursive build (rebuilds <product> and all its dependencies)
   Note: hatch and install are synonyms.
"""
   }

   private static GString packHelp() {
      return """\
   Call with:
   >> ${bold}bootstrap pack|migrate|deploy${normal} <product> <targetDirectory> [-r]
   -r            - to pack <product> and all of its dependencies
   Note: pack, migrate and deploy are synonyms.
"""
   }

   private static GString broodHelp() {
      return """\
   Call with:"
   >> ${bold}bootstrap brood|use|source${normal} <product1> [product2]...[productN] [--profile=<P1,P2,P3>]
   Note: brood, use and source are synonyms.
"""
   }

   private static GString testHelp() {
      return """\
   Call with:
   >> ${bold}bootstrap test${normal} <product>
"""
   }

   private static GString cleanHelp() {
      return """\
   Call with:
   >> ${bold}bootstrap clean${normal} <product>
"""
   }

   private static GString pluckHelp() {
      return """\
   Call with:
   >> ${bold}bootstrap pluck|uninstall${normal} <product>
"""
   }

   private static GString preenHelp() {
      return """\
   Call with:
   >> ${bold}bootstrap preen|update${normal} [--hatchery|--clutch] [--nest] (<product1> <product2> ... [-r]| --all)
   Notes: commands preen and update are synonyms; options --hatchery and --clutch are synonyms
"""
   }

   private static GString listHelp() {
      return """\
   Call with:
   >> ${bold}bootstrap list${normal} <product1> [product2]...[productN] [--profile=<P1,P2,P3>]
"""
   }

   private static GString profilesHelp() {
      return """\
   ${bold}BOOTSTRAP BUILD PROFILE SUPPORT${normal}
   ${bold}*   ${normal}BootstrapBuild supports "profiles" for certain functions. Profiles are library configurable options to
   provide different capabilities for different profiles. Profiles are useable with ishatched, hatch, brood,
   and pack commands (and of course their synonymous commands).

   ${bold}*   ${normal}Profiles are provided in one of two ways: via --profile command line arguments or in a .flockProfile file that
   may be placed in a flock's working dir (\${BOOTSTRAP_BUILD_WORKING_DIR}).

   ${bold}GLOBAL PROFILES${normal}
   ${bold}*   ${normal}Usage of --profiles is a comma separated list of profile words (with no spaces). E.g. with hatch command:

   >> bootstrap hatch foo ${bold}--profile=release,gui,c++17${normal} # (the = is optional and may instead be a space)

   ${bold}*   ${normal}Multiple profile statements may be added so the above is equivalent to:

   >> bootstrap hatch foo ${bold}--profile release --profile gui,c++17${normal} # (note lack of "=")

   ${bold}TARGETED PROFILES${normal}
   ${bold}*   ${normal}The above examples were "global" uses of profile commands. BootstrapBuild also supports "targeted"
   profiles that can be applied to one or more products. Targeted profiles are shown below:

   >> bootstrap hatch foo ${bold}--profile=release(foo;bar),debug(libA;libB)${normal}

   in this case, foo and bar are installed as releases while libA and libB are installed in debug mode.

   ${bold}*   ${normal}Targeted profiles can also be negated, so if debug mode is the default the above command is ~equivalent to:

   >> bootstrap hatch foo ${bold}--profile=release,-release(libA;libB)${normal}

   i.e., install foo and all its dependencies in release mode except libA and libB. Note that the dash
   in front of the second \"release(libA;libB)\" semantically is a minus (i.e. except)

   ${bold}PROGRAMMING BOOTSTRAP SUPPORT FILES WITH PROFILES${normal}

   ${bold}*   ${normal}Profiles support three bash test functions for use in expressions: ${bold}is${normal}, ${
      bold
   }isNot${normal}, and ${bold}add${normal}.
   ${bold}is${normal} and ${bold}add${normal} semantically do the same thing but are provided to allow more natural sounding expressions.
   Profile expressions are simply bash test expressions that leverate the above 3 functions.

         ${bold}*   ${normal}Test expressions can naturally be placed in any bootstrap build user defined script such as:
   brood-<library>.sourceme, hatch-<library>.sh, ishatched-<library>.sh, etc.

         ${bold}*   ${normal}Additionally, the prereqs-<library>.txt file may provide optional dependencies with and end test.

   E.g. a prereqs file may express an optional dependency like this: ${bold}patchelf, is linux${normal}
   a more complicated prereqs line might look like this:        ${bold}javaFX, add gui && is olderJava${normal}
"""
   }

   private static GString generalHelp() {
      return """\
${bold}BootstrapBuild - A tool to build, deploy and install anything${normal}

   RUN WITH: bootstrap <command> [options], where command is one of:

      ${bold}help${normal}                  - print this help and exit.

      ${bold}list${normal}                  - list a product's dependency list (including itself).

      ${bold}incubate|stage${normal}        - download package installation files.

      ${bold}islaid|isdownloaded${normal}   - check if source files have been downloaded to the nest.

      ${bold}lay|download${normal}          - download sources into the nest.

      ${bold}ishatched|isinstalled${normal} - check if a product is installed to the runtime or build areas.

      ${bold}hatch|install${normal}         - install a product to the runtime or build areas.

      ${bold}clean${normal}                 - clean an installed product to prepare for a fresh build.

      ${bold}pluck|uninstall${normal}       - uninstall an installed product.

      ${bold}pack|migrate|deploy${normal}   - package a product so that it is buildable on remote system.

      ${bold}brood|use|source${normal}      - source a product so that any environment is set for usage.

      ${bold}preen|update${normal}          - update hatchery and/or nest functions (e.g. could do an hg pull -u)

      ${bold}test${normal}                  - test an installed product.

   OPTIONS (can be anywhere on the commandline, before or after a command):

      ${bold}--noprofile${normal}           - ignore profile checks for operations such as pack and list.

      ${bold}--nolocalsystem${normal}       - by default, BootstrapBuild adds the local system name (Linux, Apple)
      ${bold} ${normal}                       to the profile list; this option prevents that.

      ${bold}--profile=Debug,gui${normal}   - set profile options recursively on all libraries that command
      ${bold} ${normal}                       operates on.

      ${bold}--profile=+Debug(lib1,lib2,etc)${normal}
      ${bold} ${normal}                     - set targeted profile options on a specific set of libraries.

      ${bold}--profile=-Debug(slowDep,otherDep,etc)${normal}
      ${bold} ${normal}                     - turn off a profile feature for a specific dependent library.
      ${bold} ${normal}                     - Note: this supercedes both the global profile or targeted
      ${bold} ${normal}                             profile options.
"""
   }


   public static int printHelp(String option) {

      if (option == null || option.equals("") || option.equals("help")) {
         System.out.println(generalHelp())
      }
      else {
         if (option.equals("incubate") || option.equals("stage")) {
            System.out.println(incubateHelp())
         }
         else if (option.equals("islaid") || option.equals("isdownloaded")) {
            System.out.println(islaidHelp())
         }
         else if (option.equals("lay") || option.equals("download")) {
            System.out.println(layHelp())
         }
         else if (option.equals("ishatched") || option.equals("isinstalled")) {
            System.out.println(isHatchedHelp())
         }
         else if (option.equals("hatch") || option.equals("install")) {
            System.out.println(hatchHelp())
         }
         else if (option.equals("pack") || option.equals("migrate") || option.equals("deploy")) {
            System.out.println(packHelp())
         }
         else if (option.equals("brood") || option.equals("use") || option.equals("source")) {
            System.out.println(broodHelp())
         }
         else if (option.equals("test")) {
            System.out.println(testHelp())
         }
         else if (option.equals("clean")) {
            System.out.println(cleanHelp())
         }
         else if (option.equals("pluck") || option.equals("uninstall")) {
            System.out.println(pluckHelp())
         }
         else if (option.equals("preen") || option.equals("update")) {
            System.out.println(preenHelp())
         }
         else if (option.equals("list")) {
            System.out.println(listHelp())
         }
         else if (option.equals("profiles")) {
            System.out.println(profilesHelp())
         }
         else {
            System.err.println "${bold}ERROR: Bootstrap has no command named:${normal} ${option}"
            System.err.println ""
            System.out.println(generalHelp())
            return 1
         }
      }
      return 0
   }

} // class Help

//
//######################################################
//################# BLOCK TO PHONE HOME ################
//# This can be used to find installation directory
//# no matter how this script is referenced/linked or
//# put in the PATH or whatever
//
//BASENAME=$(basename "$0")
//if [ "$0" == "${0#/}" ]
//then
//    SCRIPTS_RELATIVE_PATH="$PWD"/"${0%/*}"
//else
//    SCRIPTS_RELATIVE_PATH="${0%/*}"
//fi
//PATH_TO_EXEC="$(portableReadlink "${SCRIPTS_RELATIVE_PATH}/${BASENAME}")"
//
//# QUALIFIED_PATH is the full path to the script that contains this code.
//export BOOTSTRAP_BUILD_HOME="$(dirname "${PATH_TO_EXEC}")"
//
//############### END BLOCK TO PHONE HOME ##############
//######################################################
//
//saveddir="$(pwd)"
//
//source "${BOOTSTRAP_BUILD_HOME}/bootstraplog.shfuncs"
//if [ $? -ne 0 ] ; then
//   echo "ERROR: Cannot source BootstrapBuild Log dependencies at ${BOOTSTRAP_BUILD_HOME}" > /dev/stderr
//   exit 1
//fi
//
//readCommandLineGlobalOptions $@
//
//source "${BOOTSTRAP_BUILD_HOME}/bootstrapbuild.sourceme"
//if [ $? -ne 0 ] ; then
//   echo "ERROR: Cannot source BootstrapBuild dependencies at ${BOOTSTRAP_BUILD_HOME}" > /dev/stderr
//   exit 1
//fi
//
//
//# Next extract any profile information passed to the commandline
//# Note, this call also prunes the commandline arguments and places them
//# in the prunedArgs array
//#
//# One or more --profile arguments with global and targeted profiles.
//# Note the "=" sign is optional:
//#     --profile=debug,linux,cmake(unstruct)
//#     --profile m64(logger;config)
//readCommandLineProfiles "${prunedArgs[@]}"
//
//# Read Profiles from .flockProfile file
//#
//# The format of that file is one or more lines that look like:
//# (note that space is ignored and lines beginning with "#" are ignored)
//#
//# cmake, mex, bashBuild, release(coreFLOW;dspFLOW)
//#
//readFlockProfiles
//
//# Add local system to profiles unless explicitly told not to
//optionallyAddSystemToProfile "${prunedArgs[@]}"
//
//# Check to suppress profile checking for certain operations
//# such as list or pack commands
//checkOptionNoProfile "${prunedArgs[@]}"
//
//#echo "PROFILE LIST: ${PROFILE_LIST}"
//
//# Locate or generate a set of flock directories
//_bootstraplocatedirectories -v
//
//# Finally, select and call the appropriate bootstrap command
//selectAndCallBootstrapCommand "${prunedArgs[@]}"
