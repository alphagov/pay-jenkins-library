#!/usr/bin/env groovy

def call(
        String app = null,
        String job_name = null,
        String tag = null,
        String pay_scripts_branch = null) {

    commit = env.GIT_COMMIT ?: gitCommit()

    git_tag = tag ?: "${commit}-${env.BUILD_NUMBER}"
    job_pay_scripts_branch = pay_scripts_branch ?: (app == 'scripts') ? commit : 'master'

    build job: job_name,
            parameters: [
                    string(name: 'MODULE_NAME', value: app),
                    string(name: 'MODULE_TAG', value: git_tag),
                    string(name: 'PAY_SCRIPTS_BRANCH', value: job_pay_scripts_branch)
            ]
}
