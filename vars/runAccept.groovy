#!/usr/bin/env groovy

def call(
        String app = null,
        String tag = null,
        String pay_scripts_branch = 'master') {

    if (tag == null) {
        commit = env.GIT_COMMIT ?: gitCommit()
        tag = "${commit}-${env.BUILD_NUMBER}"
    }

    build job: 'run-separate-accept-tests',
            parameters: [
                    string(name: 'MODULE_NAME', value: app),
                    string(name: 'MODULE_TAG', value: tag),
                    string(name: 'PAY_SCRIPTS_BRANCH', value: pay_scripts_branch)
            ]
}
