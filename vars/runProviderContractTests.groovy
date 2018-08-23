#!/usr/bin/env groovy

def call() {

    echo 'Running provider contract tests'
    def commit = gitCommit()
    withCredentials([
            string(credentialsId: 'pact_broker_username', variable: 'PACT_BROKER_USERNAME'),
            string(credentialsId: 'pact_broker_password', variable: 'PACT_BROKER_PASSWORD')]
    ) {
        sh "mvn test -DrunContractTests -DPACT_BROKER_USERNAME=${PACT_BROKER_USERNAME} -DPACT_BROKER_PASSWORD=${PACT_BROKER_PASSWORD} -DPROVIDER_SHA=${commit} -Dpact.verifier.publishResults=true"
    }
}
