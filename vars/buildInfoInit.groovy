
def call(Object[] args){
    rtBuildInfo (
        captureEnv: true,
        buildName: args[0].build_name,
        buildNumber: args[0].build_number
    )
}