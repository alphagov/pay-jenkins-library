def call( String service,
          String gitSha,
          String tag) {

    withCredentials([
            string(credentialsId: 'pact_broker_username', variable: 'PACT_BROKER_USERNAME'),
            string(credentialsId: 'pact_broker_password', variable: 'PACT_BROKER_PASSWORD')]
    ) {
        def resultJson = sh(
                script: "curl -H \'Accept: application/json, application/hal+json\' --user ${PACT_BROKER_USERNAME}:${PACT_BROKER_PASSWORD} -g -s \'https://pact-broker-test.cloudapps.digital/matrix?q[][pacticipant]=${service}&q[][version]=${gitSha}&latestby=cvp&latest=true&tag=${tag}\'",
                returnStdout: true
        ).trim()
        def isDeployable = sh(
                script: "echo \'${resultJson}\' | jq \'.summary.deployable\'",
                returnStdout: true
        ).trim()
        if (isDeployable != "true") {
            error(resultJson)
        }
    }
}
