String call() {
  if (env.GIT_BRANCH == null) {
    env.GIT_BRANCH= sh(script: "git rev-parse --abbrev-ref HEAD", returnStdout: true).trim()
  }
  env.GIT_BRANCH
}
