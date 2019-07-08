String call() {
  sh(script: "git symbolic-ref -q HEAD --short", returnStdout: true).trim()
}
