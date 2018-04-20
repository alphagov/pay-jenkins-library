String call() {
  sh(script: "git rev-parse HEAD | git branch -a --contains | sed -n 2p | cut -d'/' -f 3", returnStdout: true).trim()
}
