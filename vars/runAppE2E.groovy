#!/usr/bin/env groovy

def call(
        String app = null,
        String job_name = null,
        String tag = null,
        String pay_scripts_branch = null) {

    commit = env.GIT_COMMIT ?: gitCommit()

    if (tag == null) {
        tag = "${commit}-${env.BUILD_NUMBER}"
    }

    if (pay_scripts_branch == null) {
        pay_scripts_branch = (app == 'scripts') ? commit : 'master'
    }

    build job: job_name,
            parameters: [
                    string(name: 'MODULE_NAME', value: app),
                    string(name: 'MODULE_TAG', value: tag),
                    string(name: 'PAY_SCRIPTS_BRANCH', value: pay_scripts_branch)
            ]
}
