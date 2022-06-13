def call(Object[] args) {
   String pkg_name = args[0].packageName
   String pkg_repo = args[0].packageRepo ?: 'default-npm-local'

   def npm_files = sh(returnStdout: true, script: "find ./ -maxdepth 1  -name '*.tgz'")
   def npm_list = npm_files.split()

   npm_list.each { file -> 
      file = file.split('/')[-1]
      def pkg_version = get_package_version(file, pkg_name)
      print "Pushing $file to artifactory under $pkg_repo ..."
      def file_spec = """{
         \"files\": [
               {
               \"pattern\": \"$file\",
               \"target\": \"$pkg_repo/$pkg_name/-/\",
               \"props\": \"npm.name=$pkg_name;npm.version=$pkg_version\"
               }
         ]
      }"""
      print "File spec: \n$file_spec"
      rtUpload(
         serverId: 'test-artifactory',
         spec: file_spec,
         failNoOp: true
      )
   }
}

@NonCPS
def get_package_version(def file_name, def pkg_name) {
   def match_build_identifier = file_name =~ /(${pkg_name})-(.*)(.tgz)/
   return match_build_identifier[0][2]
}
