def call(String metricPrefix) {
    def long duration = (System.currentTimeMillis() - currentBuild.startTimeInMillis) / 1000;
    postMetric("${metricPrefix}.time", duration, "new")
    postMetric("${metricPrefix}.success", 1, "new")
}
