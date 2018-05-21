#!/usr/bin/env groovy

def call(String aws_profile = "test", boolean promoted_env = true) {
  //Temporarily disabling to unblock pipeline, to allow diagnosis of 
  //DD connection problems

  //runSmokeTest(
  //        "uk.gov.pay.endtoend.categories.SmokeDirectDebitPayments",
  //        aws_profile,
  //        promoted_env
  //)
}
