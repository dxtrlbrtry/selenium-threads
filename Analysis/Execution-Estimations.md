## Estimate test execution time

```
Calculate the execution time for UI and API tests.
```

The execution time of UI tests for a suite that is targeting a specific functionality should last up to 10 minutes. A longer duration might interrupt the workflow of the developer relying on the test results.</br>
While a suite testing all functionalities of the project may take between 30 minutes and 1 hour depending on the project's complexity and the coverage of the tests.

The execution time of API tests may take between 1 and 3, depending on the target coverage and the nature of the api calls.

### UI Test duration

A UI test's duration can be anywhere between 20 seconds and 3 minutes, with an average duration of 1 minute for the majority of the tests.</br>
Longer tests should be broken down into smaller test cases.

Startup events:

- Spawn a webdriver and start up a browser ( 1 to 3 seconds )
- Perform login ( 3 to 10 seconds )
- Navigate to target section ( 5 to 10 seconds )

The duration of performing the actions to fulfill the test objective varies a lot test by test.

#### API Test duration

An API test's duration can be anywhere between 0 and 10 seconds, with an average of 1 second per test.</br>

Durations longer than 1 second can only be expected in the following cases:

- API calls are made to a remote server ( not hosted in local network ).
- Large amount of data transaction between test and API.
- Complex database query on a large data set is executed by the API. In case of identifying such a query, it should be optimized.

## Ratio and Execution

```
Explain the ideal ration of UI tests to API tests and how they should be executed.
```

### Test objectives

The ideal ratio between API and UI tests is approximately 1 UI test for 5 API tests.</br>
The UI test focuses mainly on validating the data transactions with the API, and visibility of key elements, and should not focus on testing multiple variations of similar input data unless it causes different outcome on the UI side.</br>
While the UI tests are navigating and performing actions throughout the application, it may be communicating its back-end server. While the data is automatically interpreted by the client in most cases, there are many edge cases that are difficult or time-consuming to cover with UI tests. These cases should be covered by the API tests.</br>
Besides the edge cases, all the expected positive and negative cases should be covered by the API tests, because the API tests are generally run before UI tests, and in case of any failure the feedback is provided much faster to the development team.

### Execution strategy

API tests can be executed on-demand during development, and may be added to a pre-push hook of the project. If the API test check is not mandatory before pushing, it must be added as a pull request check.
UI tests should be also executed as a pull request check.

The full suite of both UI and API tests should be executed before a release

## Growth Over Sprints

```
Estimate the execution time in the first sprint and the 12th sprint, considering steady growth in the number of test cases
```

By the end of the first sprint there should be around 10 UI tests and 30 API tests.</br>
The execution time of all the tests should be under 1 minute. The focus should be to ensure the stability of the framework and the test environment to easily support future tests.</br>
Assuming a steady linear growth ( which is rarely the case ), by the end of the 12th sprint there will be around 120 UI tests and 360 API tests.</br>
After 12 sprints, it is not always feasible to execute a the full test suite due to it's long duration, and the redundancy of some of the tests.</br>
By this time the tests should be properly tagged based on what test objectives they are fulfilling and when running them it should be easily configurable what test objectives the test run needs to cover.

## Test Data Generation

```
Consider that both UI and API tests use APIs to geneerate their own test data and are encapsulated
```

Using static test data may cause the tests to miss defects they would catch if they wouuld use dynamic data.</br>
Dynamic test data is not always an option. Filling credentials, filling fields with limited options might require very specific input.</br>
For these limited data sets a large pool of options should be uploaded to the data provider API so it can either cycle through or randomize the options when providing them to the tests.</br>
If the test data is not so strict, and it requires to fulfill only some specific constraints, it can be randomly generated, or a large pool can be uploaded to the data provider.</br>
Ideally the data provider API lives in the same network where the tests are running so the communication can be near instant.

## Provide Assumptions

```
State any assumptions made during the estimation process.
```

- There aren't any UI or API tests in the first sprint
- Sprint duration is 2 weeks ( 10 working days )
- 10 Tests can run in parallel
- The project has an authentication flow
- There is an existing test framework which TA Engineers can use
- There are 2 TA Engineers dedicated to writing tests
