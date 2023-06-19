pipeline {
    agent { docker { image 'cypress/base:16.13.0' } }
    stages {
        stage('Setup..') {
            steps { 
                sh 'rm -rf /home/node/.cache && rm -rf node_modules && npm install --save-dev'
                sh 'npx browserslist@latest --update-db'
            }
        }
        stage('must-pass-tests-in-all-specs') {
            when { expression { runWhat == 'must-pass-tests-in-all-specs' } }
            parallel {
                stage('running spec batch 1') {
                    steps {
                        buildName "MUST-ALL tests -> ${testSite}: ${username}"
                        sh "npx cypress run --env grep=MUST,qaUserName=${userName},qaUserPassword=${password},configFile=${testSite} --spec=cypress/tests/api/core_operations/aws-terraform.spec.js,cypress/tests/api/core_operations/az-terraform.spec.js,cypress/tests/api/core_operations/crud-operations/crud-ops.spec.js,cypress/tests/api/compliance/compliance.spec.js"
                    }
                }
                stage('running spec batch 2') {
                    steps {
                        sh "npx cypress run --env grep=MUST,qaUserName=${userName},qaUserPassword=${password},configFile=${testSite} --spec=cypress/tests/api/zen/validate-tfstate-mapping.spec.js,cypress/tests/api/integrations/integrations.spec.js,cypress/tests/api/integrations/jira.spec.js,cypress/tests/api/policies/custom-policy.spec.js,cypress/tests/api/policies/custom-policy-group.spec.js"
                    }
                }
                stage('running spec batch 3') {
                    steps {
                        sh "npx cypress run --env grep=MUST,qaUserName=${userName},qaUserPassword=${password},configFile=${testSite} --spec=cypress/tests/api/core_operations/tfstate/tfstate.spec.js,cypress/tests/api/core_operations/gcp-terraform.spec.js,cypress/tests/api/core_operations/aws-non-tf-engines.spec.js,cypress/tests/api/core_operations/external-apis.spec.js,cypress/tests/api/defects/defects-batch1.spec.js"
                    }
                }
            }
        }
        stage('must-pass-tests-in-selected-specs') {
            when { expression { runWhat == 'must-pass-tests-in-selected-specs' } }
            steps {
                buildName "MUST-SELECTIVE tests -> ${testSite}: ${username}"
                sh 'npx cypress cache clear && rm -rf node_modules && npm install --save-dev'
                sh "npx cypress run --env grep=MUST,qaUserName=${username},qaUserPassword=${password},configFile=${testSite} --spec=${commaSeparatedSpecPaths}"
            }
        }
        stage('on-prem-scanner-specs') {
            when { expression { runWhat == 'on-premise-scanner-specs' } }
            steps {
                buildName "ONPREM-SCANNER tests -> ${testSite}: ${username}"
                sh 'npx cypress cache clear && rm -rf node_modules && npm install --save-dev'
                sh "npx cypress run --env grep=ONPREM-SCANNER,qaUserName=${username},qaUserPassword=${password},configFile=${testSite} --spec=cypress/tests/api/core_operations/onpremise-code-scanner.spec.js"
            }
        }
    }
    post {
        always {
            junit 'cypress/report/*.xml'
        }
    }
}