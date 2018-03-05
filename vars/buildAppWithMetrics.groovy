#!/usr/bin/groovy

def call(body) {
    // Metrics
    def metricsUtils = new MetricsUtils()

    // evaluate the body block, and collect configuration into the object
    def config = [:]
    body.resolveStrategy = Closure.DELEGATE_FIRST
    body.delegate = config
    body()

    def registry = config.registry ?: "docker.io";
    def docker_repo =  config.docker_repo ?: "govukpay" ;
    def push_image =  config.push_image ?: true ;
    def disable_docker_cache =  config.disable_docker_cache ?: false ;
    def app = config.app ;
    def build_flags = "";
    def commit = gitCommit();
    def version = "${commit}-${env.BUILD_NUMBER}"

    if (disable_docker_cache == true) {
        build_flags = "--no-cache --pull"
    }

    if (app == "frontend" || app == "selfservice" || app == 'products-ui' || app == 'directdebit-frontend') {
        def buildImageName = "build-and-test-${app}"
        def Process unitTestsStatusProc = "sh docker build --file docker/build_and_test.Dockerfile -t ${buildImageName}:${version} .".execute()
        unitTestsStatusProc.waitFor()
        sh "docker run --volume \$(pwd):/app ${buildImageName}:${version}"

        Date unitTestsStopTime = new Date()
        long unitTestsDiff = (unitTestsStopTime.getTime() - metricsUtils.startTime.getTime()) / 1000;
        if (unitTestsStatusProc.exitValue()) {
            metricsUtils.postMetricToGraphite("${app}.unit-tests.failure", 1, "new")
        } else {
            metricsUtils.postMetricToGraphite("${app}.unit-tests.success", 1, "new")
            metricsUtils.postMetricToGraphite("${app}.unit-tests.time", $unitTestsDiff, "new")
        }
    }

    def imageName = "${registry}/${docker_repo}/${app}"
    def Process statusProc = "sh docker build ${build_flags} -t ${imageName}:${version} .".execute()
    statusProc.waitFor()

    Date stopTime = new Date()
    long diff = (stopTime.getTime() - metricsUtils.startTime.getTime()) / 1000;
    if (statusProc.exitValue()) {
        metricsUtils.postMetricToGraphite("${app}.docker-build.failure", 1, "new")
    } else {
        metricsUtils.postMetricToGraphite("${app}.docker-build.success", 1, "new")
        metricsUtils.postMetricToGraphite("${app}.docker-build.time", $diff, "new")
    }
}
