#!/usr/bin/env groovy

def call(String microservice, String aws_profile = "test", String tag = null) {
  tag = tag ?: gitCommit()

  build job: 'trigger-deploy-notification',
    parameters: [
      string(name: 'AWS_PROFILE', value: aws_profile),
      string(name: 'REPO', value: microservice),
      string(name: 'GIT_SHA', value: tag),
      string(name: 'DEPLOYER', value: ${BUILD_USER})
    ]
}
