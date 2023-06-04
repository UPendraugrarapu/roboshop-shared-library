def call () {
    if (!env.sonar_extra_opts){
        env.sonar_extra_opts=""
    }
    node ('workstation'){
        try {

            stage ('Check out Code'){
                sh 'ls -l'
                cleanWs()
                sh 'ls -l'
                git branch: 'main', url: 'https://github.com/upendraugrarapu/cart'
                sh 'ls -l'

//                ls -l is to trouble shoot
            }
            sh 'env'
           if (env.BRANCH_NAME != "main") {
               stage(Compile / Build) {
                   common.compile()
               }
           }

            stage('Test Cases') {
                common.testcases()
            }
            stage('Code Quality') {
                common.codequality()
            }
        }catch (e) {
            mail body: "<h1>${component}-Pipeline Failed \n ${BUILD_URL}</h1>", mimeType: 'text/html', from: 'ugrarapuupendra@gmail.com', subject: "${component}-Pipeline Failed", to: 'ugrarapuupendra@gmail.com'
        }

    }
}