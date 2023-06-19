# Build Acceptance API Tests - Cypress
- Branch release/old-version: applies to Accurics 1.3.x
- Branch master applies to Accurics 2.0

## Libraries and plugins used

- [Cypress](https://www.cypress.io/)

## Dependencies

To run the tests, please ensure you have the following installed to the latest:

- Node
- NPM

## How to run

```sh
## Install dependencies
npm i

## Open test runner with devenv config (default)
"npm run cy:open" : open cypress runner with devenv.accurics.com config

## Run Must Pass tests when captcha is enabled OR on tenable
npx cypress run --env grep=MUST,qaUserName=<tio_userName>,qaUserPassword=<tio_userPassword>,configFile=qa-milestone

## Run all tests when on tenable console
npx cypress run --env grep=BAT,configFile=qa-milestone
```

## Pre-requisites (cypress.env.json)
- captcha disabled, for no manual intervention
- pre-configured tenant => gitHub/bitbucket/gitlab/ADO/BOT integrations pre-configured in a tenant
- running onpremise-scanner instance authorized with all ^^ server editions (for onprem-scanner specs to run as expected)