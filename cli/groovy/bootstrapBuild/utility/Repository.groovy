////////////////////////////////////////////////////////////////////////////
// Helper functions for operating on repos - these should be preferred over
//   direct calls to hg, git, etc, as they may be used to track how files
//   are collected and thus allow BootstrapBuild files to be converted to
//   other build types.
//
// BootstrapBuild - a powerful but easy tool to build and install modular
//   applications in a Unix environment that supports Bash shell.
//
// Copyright FogRising 2020
// Authors: Ryan Fogarty, Charles J. Remeikas
////////////////////////////////////////////////////////////////////////////

package bootstrapBuild.utility;

import java.util.logging.Logger

class Repository {

   static Logger logger = Logger.getLogger("")

   private static int basicGet(String url,String artifactName,String tool,String command,String product,File parentdir) {
      def proc = "which $tool".execute();
      def outputStream = new StringBuffer();
      proc.waitForProcessOutput(outputStream, System.err);
      if(outputStream.toString().size() > 0) {
         println "Executing command: $tool $command $url $artifactName (in parentdir=$parentdir)"
         def process = "$tool $command $url $artifactName".execute(null,parentdir);
         def stdOutput = new StringBuffer();
         def stdError = new StringBuffer();
         process.waitForProcessOutput(stdOutput,stdError);
         if(process.exitValue() != 0) {
            logger.warning(stdOutput.toString())
            logger.warning(stdError.toString())
         }
      }
      else {
         logger.warning("$product does not appear to be installed on this system.")
         logger.warning("Request to $tool $command repo:$url failed.")
         return 1
      }
   }

   public static int hgGet(String url,String artifactName,File parentdir) {
      return basicGet(url,artifactName,"hg","clone","Mercurial",parentdir)
   }

   public static int gitGet(String url,String artifactName,File parentdir) {
      return basicGet(url,artifactName,"git","clone","Git",parentdir)
   }

   public static int svnGet(String url,String artifactName,File parentdir) {
      return basicGet(url,artifactName,"svn","checkout","Subversion",parentdir)
   }

//   public static int cvsGet(String url,String artifactName,File parentdir) {
//      // TODO: this may also fail because CVS root pserver is not configured
//      // wondering if I should write a warning like the below. Or simply allow
//      // CVS to fail (and thus return a non-zero retval).
//      return basicGet(url,artifactName,"cvs","checkout","Subversion",parentdir)
//   }
//
   public static int httpGet(String url,String artifactName,File parentdir) {
      def proc = "which curl".execute();
      def outputStream = new StringBuffer();
      proc.waitForProcessOutput(outputStream, System.err);
      if(outputStream.toString().size() > 0) {
         def process = "curl --location-trusted -O $url $artifactName".execute(null,parentdir);
         return process.exitValue()
      }
      else {
         def proc2 = "which wget".execute();
         def outputStream2 = new StringBuffer();
         proc.waitForProcessOutput(outputStream2, System.err);
         if(outputStream2.toString().size() > 0) {
            def process = "wget $url $artifactName".execute(null,parentdir);
            return process.exitValue()
         }
         else {
            logger.warning("Neither curl nor wget appear to be installed on this system.")
            logger.warning("Request to download file:$url failed.")
            return 1
         }
      }
   }

   enum RepositoryType {
      UNKNOWN,
      Git,
      Mercurial,
      Subversion,
      CVS,
      Webdav
   }

   final RepositoryType type;
   final String url;

   Repository(String typeString,String urlString) {
      if(typeString.toLowerCase().equals("git")) type = RepositoryType.Git;
      else if(typeString.toLowerCase().equals("hg")) type = RepositoryType.Mercurial;
      else if(typeString.toLowerCase().equals("svn")) type = RepositoryType.Subversion;
      else if(typeString.toLowerCase().equals("dav")) type = RepositoryType.Webdav;
      url = urlString;
   }
}

