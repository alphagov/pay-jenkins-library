#!/usr/bin/env groovy

def call(String aws_profile = "test", boolean promoted_env = true) {
  runSmokeTest(
          "uk.gov.pay.endtoend.categories.SmokeDirectDebitPayments",
          aws_profile,
          promoted_env
  )
}
