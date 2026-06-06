// Jenkinsfile - Trade Ledger Service (Java)
// Build WAR, run integration tests,
// deploy to Tomcat via SCP.
//
// Deploy targets:
//   dev:     fi-dev-app01.internal.ubs.net
//   staging: fi-stg-app01.internal.ubs.net
//   prod:    fi-prd-app01.internal.ubs.net
//            fi-prd-app02.internal.ubs.net
//
// Tomcat lives at /opt/tomcat7 on all hosts.
// WAR is deployed to webapps/ directory.
//
// Contact: platform-eng@ubs-internal.net
// Last updated: 2013-11-08

pipeline {
    agent {
        label 'rhel6-java7-maven3'
    }

    tools {
        maven 'Maven-3.0.5'
        jdk 'JDK-1.7.0_80'
    }

    environment {
        DEPLOY_USER = 'svc-deploy'
        DEPLOY_KEY = credentials(
            'fi-deploy-ssh-key'
        )
        TOMCAT_HOME = '/opt/tomcat7'
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build') {
            steps {
                sh '''
                    mvn clean package \
                        -DskipTests
                '''
            }
        }

        stage('Integration Tests') {
            steps {
                sh '''
                    mvn verify \
                        -Dsybase.host=\
10.2.48.101 \
                        -Dsybase.port=5000
                '''
            }
            post {
                always {
                    junit(
                        '**/surefire-reports'
                        + '/*.xml'
                    )
                }
            }
        }

        stage('Deploy to Dev') {
            when {
                branch 'develop'
            }
            steps {
                sh '''
                    scp -i ${DEPLOY_KEY} \
                        -o \
StrictHostKeyChecking=no \
                        target/\
trade-ledger-service.war \
                        ${DEPLOY_USER}@\
fi-dev-app01.internal.ubs.net:\
${TOMCAT_HOME}/webapps/
                    ssh -i ${DEPLOY_KEY} \
                        -o \
StrictHostKeyChecking=no \
                        ${DEPLOY_USER}@\
fi-dev-app01.internal.ubs.net \
                        "${TOMCAT_HOME}/\
bin/shutdown.sh; \
sleep 5; \
${TOMCAT_HOME}/bin/startup.sh"
                '''
            }
        }

        stage('Deploy to Staging') {
            when {
                branch 'release/*'
            }
            steps {
                sh '''
                    scp -i ${DEPLOY_KEY} \
                        -o \
StrictHostKeyChecking=no \
                        target/\
trade-ledger-service.war \
                        ${DEPLOY_USER}@\
fi-stg-app01.internal.ubs.net:\
${TOMCAT_HOME}/webapps/
                '''
            }
        }

        stage('Deploy to Prod') {
            when {
                branch 'master'
            }
            steps {
                input message: (
                    'Deploy to production?'
                )
                sh '''
                    for HOST in \
fi-prd-app01.internal.ubs.net \
fi-prd-app02.internal.ubs.net; do
                        scp \
-i ${DEPLOY_KEY} \
-o StrictHostKeyChecking=no \
target/trade-ledger-service.war \
${DEPLOY_USER}@${HOST}:\
${TOMCAT_HOME}/webapps/
                        ssh \
-i ${DEPLOY_KEY} \
-o StrictHostKeyChecking=no \
${DEPLOY_USER}@${HOST} \
"${TOMCAT_HOME}/bin/shutdown.sh; \
sleep 5; \
${TOMCAT_HOME}/bin/startup.sh"
                    done
                '''
            }
        }
    }

    post {
        failure {
            mail(
                to: 'platform-eng'
                    + '@ubs-internal.net',
                subject: (
                    "FAILED: "
                    + "${env.JOB_NAME}"
                    + " #${env.BUILD_NUMBER}"
                ),
                body: (
                    "Build: "
                    + "${env.BUILD_URL}"
                )
            )
        }
    }
}
