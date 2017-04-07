#!/usr/bin/groovy

def call(body) {
    // evaluate the body block, and collect configuration into the object
    def config = [:]
    body.resolveStrategy = Closure.DELEGATE_FIRST
    body.delegate = config
    body()

    def registry = config.registry ?: "docker.io";
    def docker_repo =  config.docker_repo ?: "govukpay";
    def push_image =  config.push_image ?: true;
    def app = config.app;

    def commit = gitCommit();
    def branch = gitBranch();
    def version = "${commit}-${env.BUILD_NUMBER}"

    def imageName = "${registry}/${docker_repo}/${app}"

    if (push_image == true) {
        // we should use img.tag / img.push here but there is an open issue
        // see: https://github.com/jenkinsci/docker-workflow-plugin/pull/90
        sh "docker push ${imageName}:${version}"

        sh "docker tag ${imageName}:${version} govukpay/frontend:${commit}"
        sh "docker push ${imageName}:${commit}"

        if (branch == "master") {
            sh "docker tag ${imageName}:${version} ${imageName}:latest-${branch}"
            sh "docker push ${imageName}:latest-${branch}"
        }
    }
    
}
