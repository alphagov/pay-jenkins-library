#!/usr/bin/env groovy

def call(String microservice, String cf_space, String tag = null, boolean tagAfterDeployment = false) {
    tag = tag ?: gitCommit()
    
    build job: 'deploy-paas-pipeline-microservice',
        parameters:[
          string(name: 'MICROSERVICE', value: microservice),
          string(name: 'CONTAINER_VERSION', value: tag),
          string(name: 'CF_SPACE', value: cf_space),
          booleanParam(name: 'TAG_AFTER_DEPLOYMENT', value: tagAfterDeployment),
        ]
}
