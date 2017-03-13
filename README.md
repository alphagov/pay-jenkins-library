# What

pay-jenkins-library is a Jenkins library helper, intended to be used
with GOV.UK Pay Jenkins.

This library gets automatically loaded and made available via Jenkins global libraries.

If you want to have Jenkinsfile pull in a branch of this repo, add the below to your
Jenkinsfile.

```
@Library('pay-jenkins-library@<BRANCH_NAME>') _
```
