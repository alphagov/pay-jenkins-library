#!/usr/bin/groovy

def call(body) {
    // Metrics
    Date startTime = new Date()

    // evaluate the body block, and collect configuration into the object
    def config = [:]
    body.resolveStrategy = Closure.DELEGATE_FIRST
    body.delegate = config
    body()

    def registry = config.registry ?: "docker.io"
    def docker_repo = config.docker_repo ?: "govukpay"
    def disable_docker_cache = config.disable_docker_cache ?: false
    def app = config.app
    def build_flags = ""
    def commit = gitCommit()
    def branch_name = gitBranchName()
    def version = "${commit}-${env.BUILD_NUMBER}"

    if (disable_docker_cache == true) {
        build_flags = "--no-cache --pull"
    }

    def imageName = "${registry}/${docker_repo}/${app}"
    sh "docker build ${build_flags} -t ${imageName}:${version} ."

    Date stopTime = new Date()
    long buildDiff = (stopTime.getTime() - startTime.getTime()) / 1000;

    postMetric("${app}.docker-build.success", 1)
    postMetric("${app}.docker-build.time", buildDiff)
}
