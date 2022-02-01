#!/usr/bin/env groovy

def call(String app = null, String tag = null) {

    commit = env.GIT_COMMIT ?: gitCommit()
    library = new File(getClass().protectionDomain.codeSource.location.path).getParentFile().parent

    env.COMPOSE_FILE = "${library}/resources/cypress/cypress.yml"
    env.COMPOSE_PROJECT_NAME = env.BUILD_TAG
    env.NETWORK_NAME = env.BUILD_TAG
    env.IMAGE = app
    env.TAG = (tag == null) ? "${commit}-${env.BUILD_NUMBER}" : tag

    sh """
            docker-compose pull --ignore-pull-failures --quiet
            docker-compose up --no-color --exit-code-from cypress
       """
}

def cleanUp() {
    sh """
            docker-compose logs || :
            docker-compose down || :
       """
}
