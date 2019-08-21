#!/usr/bin/env groovy

def call(String microservice, String aws_profile = "test", String tag = null) {
  tag = tag ?: gitCommit()

  build job: 'run-tag-and-capture-notes-commit-based',
    parameters: [
      string(name: 'ENVIRONMENT', value: aws_profile),
      string(name: 'COMMIT_HASH', value: tag),
      string(name: 'SERVICE_TO_TAG', value: microservice)
    ]
}
