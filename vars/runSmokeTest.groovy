#!/usr/bin/env groovy

def call(String smoke_test) {
  build job: 'run-smoke-test',
    parameters: [
      string(name: 'SMOKE_TEST', value: smoke_test)
    ]
}
