#!/usr/bin/env groovy

def call(String microservice, String aws_profile, String tag = null, boolean tagAfterDeployment = false) {
    tag = tag ?: "latest-master"
    
    build job: 'deploy-pipeline-microservice',
        parameters:[
          string(name: 'MICROSERVICE', value: microservice),
          string(name: 'CONTAINER_VERSION', value: tag),
          string(name: 'AWS_PROFILE', value: aws_profile),
          booleanParam(name: 'TAG_AFTER_DEPLOYMENT', value: tagAfterDeployment),
        ]
}