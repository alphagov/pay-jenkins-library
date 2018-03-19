#!/usr/bin/env groovy

def call(boolean promoted_env = true) {

    build job: 'run-smoke-test-card-payments',
            parameters:[
                    string(name: 'AWS_PROFILE', value: 'test'),
                    booleanParam(name: 'PROMOTED_ENV', value: promoted_env)
            ]
}