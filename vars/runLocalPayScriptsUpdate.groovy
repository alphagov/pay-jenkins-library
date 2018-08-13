#!/usr/bin/env groovy

def call() {
    build job: 'run-scheduled-pay-scripts-update', parameters: []
}
