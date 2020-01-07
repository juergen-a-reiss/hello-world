pipeline {
    agent any

    tools {
        // Needs to be configured in global tools.
        maven 'M3'
    }

    environment {
        //Use Pipeline Utility Steps plugin to read information from pom.xml into env variables
        IMAGE = readMavenPom().getArtifactId()
        VERSION = readMavenPom().getVersion()
        CONTAINER = "${IMAGE} +1"
    }

    options {
        timeout(time: 1, unit: 'HOURS')
    }

    stages {
        stage('Build') {
            steps {
                // Get some code from a GitHub repository
                //git 'https://github.com/juergen-a-reiss/hello-world.git'
                //git rev-parse HEAD ${GIT_COMMIT}

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

        stage('QA') {
            steps {
                timeout(time: 1, unit: 'MINUTES') {
                    script {
                        env.QA = input message: 'Deploy to QA?', ok: 'Release to QA!', submitter: 'jreiss', submitterParameter: "submitter",
                                parameters: [choice(name: 'RELEASE_SCOPE', choices: 'patch\nminor\nmajor', description: 'What is the release scope?')]
                    }
                    echo "scope: ${env.QA}"
                }
            }

        }

        stage('DEV') {
            steps {
                echo "Deploy to DEV ${VERSION} "
            }

        }


    }

    post {
        aborted {
            sh 'printenv'
            echo "TODO: Remove container ${CONTAINER} and commit  ${GIT_COMMIT}"
        }
    }

}