### Test tagging

To execute tests focusing on specific test objectives, the tests should be properly tagged with groups representing a functionality or requirement.
This enables granular execution and reporting of test cases. Each test should also have a unique identifier, which will provide the ability to run hand-picked tests, and re-run failed tests.

### Dynamic test suite

Since the TestNG input xml is not a dynamic file, it's worth considering generating the file based on a template. By generating the xml, the same template can be reused to run tests on multiple browsers, filtering tests by tag, controlling parameter values and there are many more possibilities.
The Jenkins pipeline can expose configuration values that map to TestRail's input, which are then used to generate the test xml file.

### Uploading Reports

When tests are run using TestNG, it automatically generates an xml report that is compatible with TestRail. This report can be uploaded to the TestRail server after each execution. There might be an existing Jenkins plugin that can do this, or it can also be done by a shell script using TestNG cli, or by an http request.

For test suites which last longer, it is also possible to upload results to TestRail after each test case by sending an http in an afterTest listener. This provides the possibility to monitor test runs in real-time.

Note that only 1 solution can be active at a time, either after each test, either all results after the suite is finished.
