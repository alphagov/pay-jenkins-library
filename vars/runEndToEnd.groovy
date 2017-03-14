#!/usr/bin/env groovy

def call(String app, String tag) {
    build job: 'run-end-to-end-tests',
        parameters:[
          string(name: 'MODULE_NAME', value: app),
          string(name: 'MODULE_TAG', value: tag),
          booleanParam(name: 'RUN_ZAP_TESTS', value: env.RUN_ZAP_TESTS.toBoolean())
        ]
}
