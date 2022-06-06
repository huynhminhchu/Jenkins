pipeline {
   agent any
   options {
      timeout(unit: 'MINUTES', time: 30)
   }
    environment {
        BUILD_NAME = "test_jfrog"
    }
   stages {
     stage('Pre-check') {
         steps {
            script {
               
                sh 'git clone https://github.com/rat9615/simple-nodejs-app.git'
               sh 'env | sort'
                print("This is branch fixissue_123")
               rtBuildInfo (
                    captureEnv: true, 
                    buildName: "test_jfrog_multibranch", 
                    buildNumber: "${BUILD_NUMBER}",
                    startDate: new Date(currentBuild.startTimeInMillis)
                )
                dir("simple-nodejs-app") {
                   rtNpmDeployer (
                      id: 'npm-deployer',
                      serverId: 'test-artifactory',
                      repo: 'default-npm-local',
                      // Attach custom properties to the published artifacts:
                      properties: ['npm.name=node-hello', 'npm.version=1.0.0']
                   )
                   rtNpmPublish (
                      path: '',
                      deployerId: 'npm-deployer'
                   )
                }
            }
         }
      }
   }
   post {
       always{
         rtPublishBuildInfo (
      serverId: 'test-artifactory',
      buildName: "test_jfrog_multibranch-${env.BRANCH_NAME}", 
      buildNumber: "${BUILD_NUMBER}",
   )}
      cleanup {
         print 'Cleaning up workspace directory...'
         cleanWs deleteDirs: true
      }
   }
}
