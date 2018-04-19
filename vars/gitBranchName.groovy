String call() {
  sh(script: "git rev-parse HEAD | git branch --contains | sed -n 2p", returnStdout: true).trim()
}
