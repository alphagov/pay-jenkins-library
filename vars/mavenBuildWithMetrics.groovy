#!/usr/bin/groovy
import MetricsUtils

def call(body) {
    // Metrics
    def metricsUtils = new MetricsUtils()

    // evaluate the body block, and collect configuration into the object
    def config = [:]
    body.resolveStrategy = Closure.DELEGATE_FIRST
    body.delegate = config
    body()

    def app = config.app ;

    sh 'docker pull govukpay/postgres:9.4.4'
    def Process mavenProc = "sh 'mvn clean package'".execute()
    mavenProc.waitFor()

    Date stopTime = new Date()
    long diff = (stopTime.getTime() - metricsUtils.startTime.getTime()) / 1000;
    if (mavenProc.exitValue()) {
        metricsUtils.postMetricToGraphite("${app}.maven-build.failure", 1, "new")
    } else {
        metricsUtils.postMetricToGraphite("${app}.maven-build.success", 1, "new")
        metricsUtils.postMetricToGraphite("${app}.maven-build.time", $diff, "new")
    }
}
