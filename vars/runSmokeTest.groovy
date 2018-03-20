#!/usr/bin/env groovy

def call(String smoke_tags, String aws_profile = "test", boolean promoted_env = false) {

  build job: 'run-smoke-test',
    parameters: [
      string(name: 'AWS_PROFILE', value: aws_profile),
      booleanParam(name: 'PROMOTED_ENV', value: promoted_env),
      string(name: 'SMOKE_TAGS', value: smoke_tags)
    ]
}
