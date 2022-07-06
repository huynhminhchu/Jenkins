def call(Object[] args) {
   def build_name   = ''
   def build_number = ''
   
   def is_pr = (env.BRANCH_NAME) ? env.BRANCH_NAME.matches("PR-(.*)") : False

   if (args.size() > 0) {
      switch (args[0].getClass()) {
         case String:
         case GString:
            build_name  = args[0]
            build_number = args[1]
            break
         default:
            throw new Exception('Unexpected inputs for calling artifactoryDebianPush()')
      }
   }
   build_number += env.BRANCH_NAME ? "-${env.BRANCH_NAME}" : ''

   // Push Debian packages to Artifactory sequentially
   def deb_files = sh(returnStdout: true, script: "find ./ -maxdepth 1  -name '*.deb'")
   def deb_list = deb_files.split()

   def art_repo = ''
   def package_dist = ''

   deb_list.each { file ->
      def package_version = sh(returnStdout: true, script: "dpkg -f $file Version").trim()
      if (package_version.matches("(.*)dirty(.*)")) {
         throw new Exception("Found dirty package $file. Exiting ...")
      }
   }

   deb_list.each { file ->

       if (is_pr) {
          art_repo = "pr-debian-local"
       } else {
          art_repo = "default-debian-local"
       }
       package_dist = "default-debian"

      def package_name        = sh(returnStdout: true, script: "dpkg -f $file Package").trim()
      def package_arch        = sh(returnStdout: true, script: "dpkg -f $file Architecture").trim()
      def package_pool_path   = "pool/main/" + package_name.substring(0,1) + "/" + package_name

      print "Pushing $file to artifactory under $art_repo ..."
      def package_props = "deb.distribution=$package_dist;deb.component=main;deb.architecture=$package_arch"
      if (is_pr){
         package_props += ";deb.retention_days=7"
      }
      def file_spec = """{
         \"files\": [
            {
               \"pattern\": \"$file\",
               \"target\": \"$art_repo/$package_pool_path/\",
               \"props\": \"$package_props\"
            }
         ]
      }"""
      print "File spec: \n$file_spec"
      rtUpload(
         serverId: 'test-artifactory',
         spec: file_spec,
         failNoOp: true,
         buildName: build_name,
         buildNumber: build_number
      )
   }
}
