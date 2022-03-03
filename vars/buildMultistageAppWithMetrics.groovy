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

    def buildImageName = "build-${app}-${version}"
    def imageName = "${registry}/${docker_repo}/${app}"

    sh "docker build --target=builder -t ${buildImageName} ."
    withCredentials([
        string(credentialsId: 'pact_broker_username', variable: 'PACT_BROKER_USERNAME'),
        string(credentialsId: 'pact_broker_password', variable: 'PACT_BROKER_PASSWORD')]
    ) {
        sh "docker run" +
        " --env PACT_BROKER_URL=https://pact-broker-test.cloudapps.digital" +
        " --env PACT_CONSUMER_VERSION=${commit}" +
        " --env PACT_BROKER_USERNAME=${PACT_BROKER_USERNAME}" +
        " --env PACT_BROKER_PASSWORD=${PACT_BROKER_PASSWORD}" +
        " --env PACT_CONSUMER_TAG=${branch_name}" +
        " ${buildImageName} /bin/sh -c 'npm test  --forbid-only --forbid-pending && npm run publish-pacts'"
    }

    //copy out node modules for use by cypress container \o/
    sh "docker create --name ${buildImageName} ${buildImageName}"
    sh "docker cp ${buildImageName}:/app/node_modules ./node_modules"

    sh "docker build ${build_flags} -t ${imageName}:${version} ."

    Date stopTime = new Date()
    long buildDiff = (stopTime.getTime() - startTime.getTime()) / 1000;

    postMetric("${app}.docker-build.success", 1)
    postMetric("${app}.docker-build.time", buildDiff)
}
