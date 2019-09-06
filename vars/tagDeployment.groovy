#!/usr/bin/env groovy

def call(String microservice) {
  build job: 'run-tag-and-capture-notes-commit-based',
    parameters: [
      string(name: 'ENVIRONMENT', value: 'test'),
      string(name: 'COMMIT_HASH', value: gitCommit()),
      string(name: 'SERVICE_TO_TAG', value: microservice)
    ]
}
