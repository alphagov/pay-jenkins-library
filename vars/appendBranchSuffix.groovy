/*
Append master or PR to metric prefix for completed build metrics
*/
String call(String metricPrefix) {
    if (env.BRANCH_NAME == 'master') {
        return metricPrefix + ".master"
    } else {
        return metricPrefix + ".PR"
    }
}
