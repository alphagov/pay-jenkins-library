#!/usr/bin/env groovy
def call( String providerProjectName,
          String consumerTag) {

    withCredentials([
            string(credentialsId: 'pact_broker_username', variable: 'PACT_BROKER_USERNAME'),
            string(credentialsId: 'pact_broker_password', variable: 'PACT_BROKER_PASSWORD')]
    ) {
        sh """
            set -ue pipefail
            rm -rf ${providerProjectName}
            git clone git@github.com:alphagov/${providerProjectName}.git
            cd ${providerProjectName}
            export DOCKER_HOST=unix:///var/run/docker.sock
            mvn clean test -DrunContractTests -DPACT_BROKER_USERNAME=${PACT_BROKER_USERNAME} -DPACT_BROKER_PASSWORD=${PACT_BROKER_PASSWORD} -DPACT_CONSUMER_TAG=${consumerTag} -Dpact.provider.version=\$(git rev-parse HEAD) -Dpact.verifier.publishResults=true
           """
    }
}
