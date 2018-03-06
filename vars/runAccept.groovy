#!/usr/bin/env groovy

def call(
        String app,
        String tag = null,
        String jobName = 'run-separate-accept-tests') {

    if (tag == null) {
        commit = env.GIT_COMMIT ?: gitCommit()
        tag = "${commit}-${env.BUILD_NUMBER}"
    }

    build job: jobName,
            parameters: [
                    string(name: 'MODULE_NAME', value: app),
                    string(name: 'MODULE_TAG', value: tag)
            ]
}
