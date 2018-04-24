#!/usr/bin/env groovy
def call( String providerProjectName,
          String consumerTag) {

    gitClone(gitUriFromProjectName(providerProjectName))

    withCredentials([
            string(credentialsId: 'pact_broker_username', variable: 'PACT_BROKER_USERNAME'),
            string(credentialsId: 'pact_broker_password', variable: 'PACT_BROKER_PASSWORD')]
    ) {
        sh """
            export DOCKER_HOST=unix:///var/run/docker.sock
            cd ${providerProjectName}
            mvn clean test -DrunContractTests -DPACT_BROKER_USERNAME=${PACT_BROKER_USERNAME} -DPACT_BROKER_PASSWORD=${PACT_BROKER_PASSWORD} -DPACT_CONSUMER_TAG=${consumerTag}
           """
    }
}
