class MetricsUtils {
    def metricsFilePath = "/usr/local/bin/pay-graphite-metric"
    def metricsFile = new File(metricsFilePath)
    def metricsCommand = "/bin/bash $metricsFilePath"

    Date startTime = new Date()

    def postMetricToGraphite(String metricName, int metricValue, String metricStyle) {
        if (metricsFile.exists()) {
            "$metricsCommand $metricName $metricValue $metricStyle".execute()
        }
    }
}
