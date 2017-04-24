String call() {
    if (env.ON_MASTER == null) {
        currentSha= sh(script: "git rev-parse HEAD", returnStdout: true).trim()
        masterSha= sh(script: "git rev-parse origin/master", returnStdout: true).trim()
        env.ON_MASTER= masterSha == currentSha
    }
    println ON_MASTER
    println currentSha
    println masterSha

    env.ON_MASTER
}