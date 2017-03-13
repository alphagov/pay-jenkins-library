#!/usr/bin/groovy

def call(body) {
    // evaluate the body block, and collect configuration into the object
    def config = [:]
    body.resolveStrategy = Closure.DELEGATE_FIRST
    body.delegate = config
    body()

    def registry = config.registry ?: "docker.io";
    def docker_repo =  config.docker_repo ?: "govukpay" ;
    def push_image =  config.push_image ?: true ;
    def app = config.app ;
    def build_flags = ""
    def version = "${env.REQ_COMMIT_ID}-${env.BUILD_NUMBER}"

    if (env.DISABLE_DOCKER_CACHE == true) {
        build_flags = "--no-cache --pull"
    }

    def imageName = "${registry}/${docker_repo}/${app}"
    sh "docker build -t ${build_flags} ${imageName}:${version} ."

    if (push_image == true) {
        // we should use img.tag / img.push here but there is an open issue
        // see: https://github.com/jenkinsci/docker-workflow-plugin/pull/90
        sh "docker push ${imageName}:${version}"
    }
    if (env.REQ_BRANCH_NAME == "master" && push_image == true) {
        sh "docker tag ${imageName}:${version} ${imageName}:latest-${env.REQ_BRANCH_NAME}"
        sh "docker push ${imageName}:latest-${env.REQ_BRANCH_NAME}"
    }
}
