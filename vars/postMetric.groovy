def call(String metricName, long metricValue, String metricStyle = "new") {
    def metricsFilePath = "/usr/local/bin/pay-graphite-metric.rb"
    def metricsFile = new File(metricsFilePath)
    def metricsCommand = "ruby -r ${metricsFilePath} -e \"post_metric_to_graphite"

    if (metricsFile.exists()) {
        withCredentials([
                string(credentialsId: 'graphite_api_key', variable: 'HOSTED_GRAPHITE_API_KEY'),
                string(credentialsId: 'graphite_account_id', variable: 'HOSTED_GRAPHITE_ACCOUNT_ID')]
        ) {
            sh "$metricsCommand '$metricName', '$metricValue', '$metricStyle'\""
        }
    }
}
