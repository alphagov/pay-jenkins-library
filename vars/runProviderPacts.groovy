String call(String pactBrokerUsername, String pactBrokerPassword, String tag) {
  sh(script: "./run-pact-provider-tests.sh ${pactBrokerUsername} ${pactBrokerPassword} ${tag}", returnStdout: true).trim()
}
