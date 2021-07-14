#!/usr/bin/env groovy

def call(
        String app = null,
        String e2e_test_type = null,
        String tag = null,
        String pay_scripts_branch = null) {

    commit = env.GIT_COMMIT ?: gitCommit()

    git_tag = tag ?: "${commit}-${env.BUILD_NUMBER}"
    job_pay_scripts_branch = pay_scripts_branch ?: (app == 'scripts') ? commit : 'master'

    build job: 'run-end-to-end-tests',
            parameters: [
                    string(name: 'E2E_TEST_TYPE', value: e2e_test_type),
                    string(name: 'MODULE_NAME', value: app),
                    string(name: 'MODULE_TAG', value: git_tag),
                    string(name: 'PAY_SCRIPTS_BRANCH', value: job_pay_scripts_branch)
            ]
}
