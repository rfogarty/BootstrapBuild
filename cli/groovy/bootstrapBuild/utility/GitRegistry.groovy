package bootstrapBuild.utility

class GitRegistry implements Registry {
   String mURL;

   GitRegistry(String url) {
      mURL = url;
   }

   public int load(String hatchname,File hatchery) {
      return Repository.gitGet(mURL + "/" + hatchname + "-bootstrap.git",hatchname + "-bootstrap",hatchery)
   }
}
