String call() {
  sh(script: "git symbolic-ref HEAD|sed -e 's|refs/heads/||'", returnStdout: true).trim()
}
