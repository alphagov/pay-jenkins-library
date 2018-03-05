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

    def registry = config.registry ?: "docker.io";
    def docker_repo =  config.docker_repo ?: "govukpay";
    def push_image =  config.push_image ?: true;
    def app = config.app;

    def commit = gitCommit()
    def onMaster = commit == getMasterHeadCommit()
    def version = "${commit}-${env.BUILD_NUMBER}"

    def imageName = "${registry}/${docker_repo}/${app}"

    if (push_image == true) {
        // we should use img.tag / img.push here but there is an open issue
        // see: https://github.com/jenkinsci/docker-workflow-plugin/pull/90
        sh "docker push ${imageName}:${version}"

        sh "docker tag ${imageName}:${version} ${imageName}:${commit}"
        def Process pushBuildTagProc = "sh docker push ${imageName}:${commit}".execute()
        pushBuildTagProc.waitFor()

        if (pushBuildTagProc.exitValue()) {
            metricsUtils.postMetricToGraphite("${app}.docker-tag.failure", 1, "new")
        } else {
            metricsUtils.postMetricToGraphite("${app}.docker-tag.success", 1, "new")
        }

        if (onMaster) {
            sh "docker tag ${imageName}:${version} ${imageName}:latest-master"
            def Process pushMasterTagProc = "sh docker push ${imageName}:latest-master".execute()
            pushMasterTagProc.waitFor()

            if (pushMasterTagProc.exitValue()) {
                metricsUtils.postMetricToGraphite("${app}.docker-master-tag.failure", 1, "new")
            } else {
                metricsUtils.postMetricToGraphite("${app}.docker-master-tag.success", 1, "new")
            }
        }
    }
}
