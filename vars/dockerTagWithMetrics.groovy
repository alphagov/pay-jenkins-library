#!/usr/bin/groovy

def call(body) {
    Date startTime = new Date()

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
    if (env.MASTER_HEAD_COMMIT == null) {
        env.MASTER_HEAD_COMMIT =   sh(script: "git fetch --no-tags origin +refs/heads/master:refs/remotes/origin/master; git rev-parse origin/master", returnStdout: true).trim()
    }
    def onMaster = commit == env.MASTER_HEAD_COMMIT
    def version = "${commit}-${env.BUILD_NUMBER}"

    def imageName = "${registry}/${docker_repo}/${app}"

    if (push_image == true) {
        // we should use img.tag / img.push here but there is an open issue
        // see: https://github.com/jenkinsci/docker-workflow-plugin/pull/90
        sh "docker push ${imageName}:${version}"
        sh "docker tag ${imageName}:${version} ${imageName}:${commit}"
        sh "docker push ${imageName}:${commit}"

        if (onMaster) {
            sh "docker tag ${imageName}:${version} ${imageName}:latest-master"
            sh "docker push ${imageName}:latest-master"
        }

        Date dockerStopTime = new Date()
        long dockerDiff = (dockerStopTime.getTime() - startTime.getTime()) / 1000;

        postMetric("${app}.docker-tag.success", 1)
        postMetric("${app}.docker-tag.time", dockerDiff)
    }
}
