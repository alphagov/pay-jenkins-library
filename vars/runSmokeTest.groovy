#!/usr/bin/env groovy

def call(String smoke_tags) {
  build job: 'run-smoke-test',
    parameters: [
      string(name: 'SMOKE_TAGS', value: smoke_tags)
    ]
}
