pipeline {
    agent any

    tools {
        maven 'M3'
    }

    environment {
        //Use Pipeline Utility Steps plugin to read information from pom.xml into env variables
        IMAGE = readMavenPom().getArtifactId()
        VERSION = readMavenPom().getVersion()
    }

    stages {
        stage('QA') {
            steps {
                script {
                    env.RELEASE_SCOPE = input message: 'Deploy to QA?', ok: 'Release to QA!', submitterParameter: "submitter",
                            parameters: [choice(name: 'RELEASE_SCOPE', choices: 'patch\nminor\nmajor', description: 'What is the release scope?')]
                }
                echo "scope: ${env.RELEASE_SCOPE}"
                echo "submitter: ${env.submitter}"
                //input (message: "Deploy to QA?", ok:"yes", submitter: "jreiss", submitterParameter:"submitter")
                //echo "Deploy to QA ${VERSION} by ${submitter}"
            }

        }
        stage('Build') {
            steps {
                // Get some code from a GitHub repository
                git 'https://github.com/juergen-a-reiss/hello-world.git'

                // Run Maven on a Unix agent.
                sh "mvn clean package"

                // Now we should deploy to repository
                echo "${IMAGE}"

            }

            post {
                // If Maven was able to run the tests, even if some of the test
                // failed, record the test results and archive the jar file.
                success {
                    junit '**/target/surefire-reports/TEST-*.xml'
                    archiveArtifacts 'target/*.jar'
                }
            }
        }

        stage('DEV') {
            steps {
                echo "Deploy to DEV ${VERSION}"
            }

        }


    }
}