def call(Object[] args){
    def build_number = args[0].build_number
    build_number += env.BRANCH_NAME ? "-${env.BRANCH_NAME}" : ''
    rtPublishBuildInfo(
        serverId: "test-artifactory",
        buildName: args[0].build_name,
        buildNumber: build_number
    )
}
