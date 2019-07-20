# GitHub Coverage reporter

[![Build Status](https://travis-ci.org/jnewc/github-coverage-reporter.svg?branch=master)](https://travis-ci.org/jnewc/github-coverage-reporter)

Jenkins plugin for reporting code coverage as a GitHub status check.

## Screenshots

![Screenshot of success status](https://raw.githubusercontent.com/jenkinsci/github-coverage-reporter/readme/assets/coverage-success.png)

![Screenshot of failure status](https://raw.githubusercontent.com/jenkinsci/github-coverage-reporter/readme/assets/coverage-failure.png)

# Usage

This plugin allows you to send status checks to GitHub pull requests, setting
the status based on whether it meets or exceeds expectations.

The base coverage amount against which the PR's coverage amount is compared can
be taken from one of two places:

* **SonarQube** - retrieved from the API of your SonarQube instance. The
SonarQube project can be selected from a list.
* **Fixed Value** - a fixed coverage amount specified as part of your Jenkins
job configuration.

**NOTE**: Currently the SonarQube instance must be operating locally and be
accessible from `localhost:9000`. This will be addressed in a coming release.
It is recommended that you proxy to the correct host and/or port if your
SonarQube instance is hosted elsewhere.

## Configuring the plugin
Go to **Manage** -> **Configure System** and find the section named
_GitHub Coverage Reporter_.

From here you can configure two fields:

* **GitHub Enterprise URL** (_optional_) - The full url of a GitHub enterprise instance. If left blank, public GitHub is used (i.e. github.com).
* **GitHub Access Token** (__*required*__) - A valid GitHub API token. The token should have
sufficient permissions to allow reading target repos and posting statuses to
them. If you're not sure how to create a token, read [this](https://help.github.com/articles/creating-a-personal-access-token-for-the-command-line).

## Using the plugin

The plugin provides a post-build action for auditing coverage files and
posting a GitHub status to your pull request.

![Screenshot of post-build action](https://raw.githubusercontent.com/jenkinsci/github-coverage-reporter/readme/assets/action.png)

1. Specify the path of the coverage file and the type of coverage.
2. Choose either SonarQube or Fixed Coverage.
3. If fixed coverage, enter the minimum expected coverage (e.g. 75.0 for 75%).

Open a PR and check that the job reports the correct status back to GitHub.

## Using the plugin in a Jenkinsfile

You can also use this plugin as part of Jenkins pipeline (aka Jenkinsfile). You can use it as an extra step, or as a post step.
For example for a declarative Jenkinsfile:

```
pipeline {
    stages {
    ...
        stage('Testing...') {
            steps {
                ...
            }
            post {
                success {
                    script {
                        // if we are in a PR
                        if (env.CHANGE_ID) {
                            publishCoverageGithub(filepath:'coverage.xml', coverageXmlType: 'cobertura', comparisonOption: [ value: 'optionFixedCoverage', fixedCoverage: '0.65' ], coverageRateType: 'Line')
                        }
                    }
                }
            }
        }
    ...
}
```

or if you want to add step depending on the coverage threshold result:
```
pipeline {
    stages {
    ...
        stage('Testing...') {
            steps {
                ...
            }
            post {
                success {
                    script {
                        // if we are in a PR
                        if (env.CHANGE_ID) {
                            if (publishCoverageGithub(filepath:'coverage.xml', coverageXmlType: 'cobertura', comparisonOption: [ value: 'optionFixedCoverage', fixedCoverage: '0.65' ], coverageRateType: 'Line')) {
                                sh "echo success"
                            } else {
                                sh "echo failure"
                            }
                        }
                    }
                }
            }
        }
    ...
}
```

The different publishCoverageGithub() options are:
- filepath
- coverageXmlType: `cobertura`, `jacoco` or `sonarqube`
- comparisonOption.value: `optionFixedCoverage` or `optionSonarProject`
- comparisonOption.fixedCoverage (for fixed coverage). It is a percentage between 0.0 (0%) and 1.0 (100%)
- comparisonOption.sonarProject (for Sonar). Project key name
- coverageRateType: `Line`, `Branch` or `Overall`

## License

All code is licensed under [Apache 2.0 License](LICENSE)
