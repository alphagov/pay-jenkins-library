def call( String service,
          String gitSha,
          String tag) {

    withCredentials([
            string(credentialsId: 'pact_broker_username', variable: 'PACT_BROKER_USERNAME'),
            string(credentialsId: 'pact_broker_password', variable: 'PACT_BROKER_PASSWORD')]
    ) {
        echo sh(
          script: "pact-broker can-i-deploy --pacticipant ${service} --version ${gitSha} --to ${tag} --broker-base-url https://pact-broker-test.cloudapps.digital/ -u ${PACT_BROKER_USERNAME} -p ${PACT_BROKER_PASSWORD}",
          returnStdout: true
        )
    }
}
