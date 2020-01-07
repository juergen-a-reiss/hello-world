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
        CONTAINER = "${IMAGE}-${GIT_BRANCH}-${VERSION}-${currentBuild.startTimeInMillis}-${GIT_COMMIT}"
    }

    options {
        timeout(time: 1, unit: 'HOURS')
    }

    stages {
        stage('Build') {
            steps {
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

        stage('DEVMASTER') {
            when {
                branch 'master'
            }
            steps {
                echo "Automatically deploy to DEV if master"
            }

        }

        stage('DEVNONMASTER') {
            when {
                expression {
                    return env.BRANCH_NAME != 'master'
                }
            }
            steps {
                echo "Manually deploy to DEV if not master"
            }

        }

        stage('QA') {
            steps {
                timeout(time: 1, unit: 'MINUTES') {
                    script {
                        env.QA = input message: 'Deploy to QA?', ok: 'Release to QA!', submitter: 'jreiss'  , submitterParameter: "submitter",
                                parameters: [choice(name: 'RELEASE_SCOPE', choices: 'patch\nminor\nmajor', description: 'What is the release scope?')]
                    }
                    echo "scope: ${env.QA}"
                }
            }

        }
    }

    post {
        aborted {
            sh 'printenv'
            echo "TODO: Remove container ${CONTAINER}"
        }
    }

}