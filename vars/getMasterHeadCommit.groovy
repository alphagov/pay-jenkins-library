String call() {
    if (env.MASTER_HEAD_COMMIT == null) {
        env.MASTER_HEAD_COMMIT =   sh(script: "git fetch origin +refs/heads/master:refs/remotes/origin/master; git rev-parse origin/master", returnStdout: true).trim()
    }
    env.MASTER_HEAD_COMMIT
}
