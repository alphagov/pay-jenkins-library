#!/usr/bin/env groovy

def call(String smoke_tags) {
  build job: 'run-smoke-test',
    parameters: [
      string(name: 'AWS_PROFILE', value: 'test'),
      booleanParam(name: 'PROMOTED_ENV', value: true),
      string(name: 'SMOKE_TAGS', value: smoke_tags)
    ]
}
