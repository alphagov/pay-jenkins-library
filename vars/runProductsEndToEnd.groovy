#!/usr/bin/env groovy

def call(String app, String tag = null) {
    if (tag == null) {
        commit = env.GIT_COMMIT ?: gitCommit()
        tag = "${commit}-${env.BUILD_NUMBER}"
    }

    run_zap_tests = false
    test_profile = 'end2end-products'

    build job: 'run-end-to-end-tests',
        parameters:[
          string(name: 'MODULE_NAME', value: app),
          string(name: 'MODULE_TAG', value: tag),
          booleanParam(name: 'RUN_ZAP_TESTS', value: run_zap_tests),
          string(name: 'TEST_PROFILE', value: test_profile)
        ]
}
