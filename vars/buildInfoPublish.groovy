def call(Object[] args){
    rtPublishBuildInfo(
        serverId: "test-artifactory",
        buildName: args[0].build_name,
        buildNumber: args[0].build_number
    )
}
