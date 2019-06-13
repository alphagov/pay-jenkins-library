#!/usr/bin/env groovy


#!/usr/bin/env groovy

def call(
        String app = null,
        String job_name = null,
        String tag = null,
        String pay_scripts_branch = null,
        String end_to_end_list = null) {

    commit = env.GIT_COMMIT ?: gitCommit()

    git_tag = tag ?: "${commit}-${env.BUILD_NUMBER}"
    job_pay_scripts_branch = pay_scripts_branch ?: (app == 'scripts') ? commit : 'master'

    build job: 'run-many-end-to-end',
            parameters: [
                    string(name: 'MODULE_NAME', value: app),
                    string(name: 'MODULE_TAG', value: git_tag),
                    string(name: 'PAY_SCRIPTS_BRANCH', value: job_pay_scripts_branch),
                    string(name: 'E2E_TEST_LIST', value: end_to_end_list)
            ]
}

