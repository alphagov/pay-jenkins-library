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

    if (app == "frontend" || app == "selfservice" || app == 'products-ui' || app == 'directdebit-frontend') {
        def buildImageName = "build-and-test-${app}"
        sh "docker build --file docker/build_and_test.Dockerfile -t ${buildImageName}:${version} ."
        withCredentials([
                string(credentialsId: 'pact_broker_username', variable: 'PACT_BROKER_USERNAME'),
                string(credentialsId: 'pact_broker_password', variable: 'PACT_BROKER_PASSWORD')]
        ) {
            sh "docker run --env PACT_BROKER_URL=https://pact-broker-test.cloudapps.digital --env PACT_CONSUMER_VERSION=${commit} --env PACT_BROKER_USERNAME=${PACT_BROKER_USERNAME} " +
                    "--env PACT_BROKER_PASSWORD=${PACT_BROKER_PASSWORD} --env PACT_CONSUMER_TAG=${branch_name} --volume \$(pwd):/app ${buildImageName}:${version}"
        }
        Date unitTestsStopTime = new Date()
        long unitTestsDiff = (unitTestsStopTime.getTime() - startTime.getTime()) / 1000;

        postMetric("${app}.unit-tests.success", 1)
        postMetric("${app}.unit-tests.time", unitTestsDiff)
    }

    def imageName = "${registry}/${docker_repo}/${app}"
    sh "docker build ${build_flags} -t ${imageName}:${version} ."

    Date stopTime = new Date()
    long buildDiff = (stopTime.getTime() - startTime.getTime()) / 1000;

    postMetric("${app}.docker-build.success", 1)
    postMetric("${app}.docker-build.time", buildDiff)
}
