import bootstrapBuild.commands.DependencyList
import bootstrapBuild.commands.Help
import bootstrapBuild.commands.Incubate
import bootstrapBuild.utility.Environment
import bootstrapBuild.utility.Profile

class Bootstrap {

   public static void selectAndCallBootstrapCommand(String... args) {

      String command = ""
      if (args != null && args.length > 0) { command = args[0]; }
      String[] options = null
      if (args != null && args.length > 1) {
         // trim off the first command argument
         options = args.takeRight(args.length - 1)
      }

      Profile profile = new Profile();
      options = profile.processCommandline(options);

      if (command.equals("")) {
         System.err.println("ERROR: run with: bootstrap <command> <product> [options]");
         Help.printHelp(options ? options[0] : null);
         System.exit(1)
      } else if (command.equals("help")) {
         System.exit(Help.printHelp(options ? options[0] : null))
      }

      // Expand all environment variables that were constructed by launch script
      def env = System.getenv()

      String hatchery = env["BOOTSTRAP_BUILD_HATCHERY"]
      Environment props = new Environment();
      props.BOOTSTRAP_BUILD_HATCHERY = hatchery

      String registries = env["BOOTSTRAP_BUILD_REGISTRIES"]
      for(String registry:registries.split(";")) {
         if(registry != null) props.BOOTSTRAP_BUILD_REGISTRIES.add(registry)
      }

      // Now choose the appropriate command to call
      if (command.equals("incubate") || command.equals("stage")) {

         Incubate.fetchItems(props,options)

      } else if (command.equals("islaid") || command.equals("isdownloaded")) {

      } else if (command.equals("lay") || command.equals("download")) {

      } else if (command.equals("ishatched") || command.equals("isinstalled")) {

      } else if (command.equals("hatch") || command.equals("install")) {

      } else if (command.equals("pack") || command.equals("migrate") || command.equals("deploy")) {

      } else if (command.equals("brood") || command.equals("use") || command.equals("source")) {

      } else if (command.equals("test")) {

      } else if (command.equals("clean")) {

      } else if (command.equals("pluck") || command.equals("uninstall")) {

      } else if (command.equals("preen") || command.equals("update")) {

      } else if (command.equals("list")) {
         DependencyList.printList(props,profile,options);
      }
      else {
         System.err.println "ERROR: Bootstrap has no command named: ${command}"
         System.exit(1)
      }
   }

}

Bootstrap.selectAndCallBootstrapCommand(args)


