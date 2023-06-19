pipeline {
    agent { docker { image 'cypress/base:16.13.0' } }
    stages {
        stage('Setup..') {
            steps { sh 'rm -rf /home/node/.cache && rm -rf node_modules && npm install --save-dev' }
        }
        stage('demo-tests-in-applicable-specs') {
            steps {
                // Setting build name
                buildName "DEMO tests -> ${testSite}: ${username}"
                sh 'cypress cache clear && rm -rf node_modules && npm install --save-dev'
                sh 'npx browserslist@latest --update-db'
                sh "npx cypress run --env grep=DEMO,qaUserName=${username},qaUserPassword=${password},configFile=${testSite} --spec=cypress/tests/api/core_operations/aws-non-tf-engines.spec.js,cypress/tests/api/core_operations/aws-terraform.spec.js,cypress/tests/api/core_operations/az-terraform.spec.js,cypress/tests/api/integrations/integrations.spec.js,cypress/tests/api/integrations/jira.spec.js,cypress/tests/api/core_operations/crud-operations/crud-ops.spec.js"
            }
        }
    }
    post {
        always {
            junit 'cypress/report/*.xml'
        }
    }
}