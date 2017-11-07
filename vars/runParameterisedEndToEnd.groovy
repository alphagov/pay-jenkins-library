#!/usr/bin/env groovy

def call(String app, String tag = null, testProfile = 'end2end', zapTests = true, acceptTests = true) {
    if (tag == null) {
        commit = env.GIT_COMMIT ?: gitCommit()
        tag = "${commit}-${env.BUILD_NUMBER}"
    }

    run_zap_tests = zapTests && commit == getMasterHeadCommit()

    build job: 'run-end-to-end-tests',
            parameters: [
                    string(name: 'MODULE_NAME', value: app),
                    string(name: 'MODULE_TAG', value: tag),
                    booleanParam(name: 'RUN_ZAP_TESTS', value: run_zap_tests),
                    string(name: 'TEST_PROFILE', value: testProfile),
                    booleanParam(name: 'RUN_ACCEPT_TESTS', value: acceptTests)
            ]
}
