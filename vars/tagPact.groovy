#!/usr/bin/env groovy
def call( String consumerName,
          String consumerVersion,
          String tag) {

    withCredentials([
            string(credentialsId: 'pact_broker_username', variable: 'PACT_BROKER_USERNAME'),
            string(credentialsId: 'pact_broker_password', variable: 'PACT_BROKER_PASSWORD')]
    ) {
        STATUS = sh (
                script: "curl -H \"Content-Type: application/json\" -X PUT -k --user ${PACT_BROKER_USERNAME}:${PACT_BROKER_PASSWORD} https://pact-broker-test.cloudapps.digital/pacticipants/${consumerName}/versions/${consumerVersion}/tags/${tag} --write-out \'%{http_code}\' -s -o /dev/null",
                returnStdout: true
        ).trim()
        if (!["200","201"].contains(STATUS)) {
            error("Tagging consumer pact did not return 200 OK or 201 CREATED")
        }
    }
}
