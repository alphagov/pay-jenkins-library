def call(String metricName, long metricValue, String metricStyle) {
    def metricsFilePath = "/usr/local/bin/pay-graphite-metric.rb"
    def metricsFile = new File(metricsFilePath)
    def metricsCommand = "ruby -r ${metricsFilePath} -e \"post_metric_to_graphite"

    if (metricsFile.exists()) {
      sh "$metricsCommand '$metricName', '$metricValue', '$metricStyle'\""
    }
}
