def call(Object[] args){
    def build_number = args[0].build_number
    build_number += env.BRANCH_NAME ? "-${env.BRANCH_NAME}" : ''
    rtPublishBuildInfo(
        serverId: "test-artifactory",
        buildName: args[0].build_name,
        buildNumber: build_number
    )
    def is_pr = (env.BRANCH_NAME) ? env.BRANCH_NAME.matches("PR-(.*)") : false
    def build_info_path = "artifactory-build-info/${build_name}/${build_number}-*.json"
    def build_info_props = ""
    build_info_props += is_pr  ? "deb.retention_days=7"  : ''
    rtSetProps (
      serverId: test-artifactory,
      spec: "{\"files\":[{\"pattern\":\"${build_info_path}\",\"sortBy\":[\"created\"],\"sortOrder\":\"desc\",\"limit\":1}]}",
      props: build_info_props,
      failNoOp: true
   ) 
}
