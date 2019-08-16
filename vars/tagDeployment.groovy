#!/usr/bin/env groovy

def call() {
  commit_hash = gitCommit()

  date = new java.text.SimpleDateFormat("yyyy-MM-dd-HH:mm:ss").format(new Date())

  latest_tag_components = sh(script: "git describe --abbrev=0 --match 'alpha_release-*'", returnStdout: true).trim().split('-')

  latest_tag_str = latest_tag_components.size() > 1 ? latest_tag_components[1] : ''

  new_release_number = latest_tag_str =~ /^[0-9]+$/ ? latest_tag_str.toInteger() + 1 : 1

  tag_name = "alpha_release-${new_release_number}"

  echo "Tagging ${commit_hash} with ${tag_name}"
  sh "git tag -a '${tag_name}' '${commit_hash}' -m 'release candidate tag created on ${date}'"
  sh "git push origin '${tag_name}'"
}
