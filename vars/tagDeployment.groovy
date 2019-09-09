#!/usr/bin/env groovy

// Preserve the previous method signature, even though we don't need any parameters any more
def call(String ignored = null) {
  date = new java.text.SimpleDateFormat("yyyy-MM-dd-HH:mm:ss").format(new Date())

  latest_tag_components = sh(script: '''cd \$WORKSPACE
git describe --abbrev=0 --match 'alpha_release-*'
''', returnStdout: true).trim().split('-')

  latest_tag_str = latest_tag_components.size() > 1 ? latest_tag_components[1] : ''

  new_release_number = latest_tag_str =~ /^[0-9]+$/ ? latest_tag_str.toInteger() + 1 : 1

  tag_name = "alpha_release-${new_release_number}"

  echo "Tagging with ${tag_name}"
  sh '''cd \$WORKSPACE
git tag -a '${tag_name}' -m 'release candidate tag created on ${date}'
git push origin '${tag_name}'
'''
}
