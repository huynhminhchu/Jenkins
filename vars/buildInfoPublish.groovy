def call(Object[] args){
    def build_name = args[0].build_name 
    build_name += env.BRANCH_NAME ? "-${env.BRANCH_NAME}" : ''
    rtPublishBuildInfo(
        serverId: "test-artifactory",
        buildName: build_name,
        buildNumber: args[0].build_number
    )
}
