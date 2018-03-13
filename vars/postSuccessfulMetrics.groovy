/*
Post multiple metrics
- metricPrefix defines what you want as the metric identifier
  e.g. metricPrefix = 'connector.maven-build'
       ci.ci-4.connector.maven-build.time
       ci.ci-4.connector.maven-build.success
- startTime defines start time of item you are timing
  e.g. startTimeInMillis = -1
       This will use current time - current build start time
  e.g. startTimeInMillis = xxxxx
       This will use current time - xxxx
*/
def call(String metricPrefix, long startTimeInMillis = -1) {
    def long duration = 0

    if (startTimeInMillis == -1) {
      duration = (System.currentTimeMillis() - currentBuild.startTimeInMillis) / 1000;
    } else {
      duration = (System.currentTimeMillis() - startTimeInMillis) / 1000;
    }

    postMetric("${metricPrefix}.time", duration)
    postMetric("${metricPrefix}.success", 1)
}
