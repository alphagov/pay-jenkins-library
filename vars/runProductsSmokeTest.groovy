#!/usr/bin/env groovy

def call(String aws_profile = "test", boolean promoted_env = true) {
  runSmokeTest(
          smoke_tags: "uk.gov.pay.endtoend.categories.SmokeProducts",
          aws_profile: aws_profile,
          promoted_env: promoted_env
  )
}
