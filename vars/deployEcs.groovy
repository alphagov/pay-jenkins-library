#!/usr/bin/env groovy

def call(String microservice, String aws_profile = 'test', String tag = null, boolean tagAfterDeployment = false, boolean run_tests = true, smoke_tags = '', boolean promoted_env = true) {
    tag = tag ?: gitCommit()
    
    build job: 'deploy-ecs-microservice',
        parameters:[
          string(name: 'MICROSERVICE', value: microservice),
          string(name: 'MICROSERVICE_IMAGE_TAG', value: tag),
          string(name: 'AWS_PROFILE', value: aws_profile)
        ]
}
