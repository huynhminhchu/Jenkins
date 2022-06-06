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
               rtBuildInfo (
                    captureEnv: true, 
                    buildName: "test_jfrog_multibranch", 
                    buildNumber: "${BUILD_NUMBER}",
                    startDate: new Date(currentBuild.startTimeInMillis)
                )
               buildInfoInit(build_name: "test_jfrog_multibranch", build_number: "${BUILD_NUMBER}")
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
         buildInfoPublish(build_name: "test_jfrog_multibranch", build_number: "${BUILD_NUMBER}")
       }
      cleanup {
         print 'Cleaning up workspace directory...'
         cleanWs deleteDirs: true
      }
   }
}
