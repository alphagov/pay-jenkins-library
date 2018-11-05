#!/usr/bin/env groovy

def call(
        String app = null,
        String tag = null,
        String pay_scripts_branch = null) {
    runAppE2E(app, 'run-end-to-end-card-payment-tests', tag, pay_scripts_branch)
}
