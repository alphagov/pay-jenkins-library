#!/usr/bin/env groovy
def call( String providerProjectName,
          String consumerTag) {

    gitClone(gitUriFromProjectName(providerProjectName))
    def providerSha = sh(script: "cd ${providerProjectName} && git rev-parse HEAD", returnStdout: true).trim()

    withCredentials([
            string(credentialsId: 'pact_broker_username', variable: 'PACT_BROKER_USERNAME'),
            string(credentialsId: 'pact_broker_password', variable: 'PACT_BROKER_PASSWORD')]
    ) {
        sh """
            set -ue pipefail
            cd ${providerProjectName}
            export DOCKER_HOST=unix:///var/run/docker.sock
            mvn clean test -DrunContractTests -DPACT_BROKER_USERNAME=${PACT_BROKER_USERNAME} -DPACT_BROKER_PASSWORD=${PACT_BROKER_PASSWORD} -DPACT_CONSUMER_TAG=${consumerTag} -DPROVIDER_SHA=${providerSha} -Dpact.provider.version=${providerSha} -Dpact.verifier.publishResults=true
           """
    }
}
