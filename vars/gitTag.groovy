#!/usr/bin/env groovy

def call(String tag, String annotation = null) {
    tag_cmd = "git tag -f ${tag}"

    if (annotation != null) {
      tag_cmd = tag_cmd.concat("-a -m '${annotation}'")
    }

    sh "git config user.email payments-team@digital.cabinet-office.gov.uk"
    sh "git config user.name pay-jenkins"

    sh tag_cmd
    sh "git push origin ${tag}"
}
