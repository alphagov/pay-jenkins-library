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
    def build_flags = "";
    def commit = gitCommit();
    def branch = gitBranch();
    def version = "${commit}-${env.BUILD_NUMBER}"

    if (env.DISABLE_DOCKER_CACHE == true) {
        build_flags = "--no-cache --pull"
    }

    def imageName = "${registry}/${docker_repo}/${app}"
    sh "docker build -t ${build_flags} ${imageName}:${version} ."
}
