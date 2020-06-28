package bootstrapBuild.utility

class Environment {

   String BOOTSTRAP_BUILD_HATCHERY;
   ArrayList<String> BOOTSTRAP_BUILD_REGISTRIES;

   Environment() {
      BOOTSTRAP_BUILD_REGISTRIES = new ArrayList<String>();
   }

   static File hatchery(Environment props) {
      return new File(props.BOOTSTRAP_BUILD_HATCHERY)
   }

   static boolean haveHatchery(Environment props) {
      File f = new File(props.BOOTSTRAP_BUILD_HATCHERY);
      return f.exists() && f.isDirectory();
   }

   static File ovumdir(Environment props, String hatchname) {
      return new File(props.BOOTSTRAP_BUILD_HATCHERY + "/" + hatchname + "-bootstrap")
   }

   static File dependsfile(Environment props, String hatchname) {
      return new File(props.BOOTSTRAP_BUILD_HATCHERY + "/" + hatchname + "-bootstrap/prereqs.txt")
   }

   static File hatchfile(Environment props, String hatchname) {
      return new File(props.BOOTSTRAP_BUILD_HATCHERY + "/" + hatchname + "-bootstrap/prereqs.txt")
   }

}
