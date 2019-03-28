String call(gitUri) {
    sh(script: "git clone ${gitUri}", returnStdout: true)
}
