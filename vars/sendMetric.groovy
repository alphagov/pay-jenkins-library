def call(String metricName, String metricValue) {

    withCredentials([
            string(credentialsId: 'graphite_api_key', variable: 'HOSTED_GRAPHITE_API_KEY'),
            string(credentialsId: 'graphite_account_id', variable: 'HOSTED_GRAPHITE_ACCOUNT_ID')]
    ) {

        sh "echo ${HOSTED_GRAPHITE_API_KEY}.${metricName} ${metricValue} | nc ${HOSTED_GRAPHITE_ACCOUNT_ID}.carbon.hostedgraphite.com 2003"
    }
}
