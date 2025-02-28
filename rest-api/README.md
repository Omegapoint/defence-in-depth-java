This repo demonstrates the patterns and concepts for REST APIs from https://omegapoint.se/defence-in-depth-secure-apis

DEMO:

1. Validate request
2. Validate token 
3. Transform token 
4. Validate input data 
5. Validate permissions to perform operation 
6. Validate permissions to access data 
7. Domain Driven Security 
8. Secrets 
9. Complete + Tests

Note that test in this repo aims to highlight that security is part of your domain and non-functional requirements. Thus we should identify requirements, test cases and add tests to prove that our defence layers works as expected.

In this repo we have added tests in the last module 9 with two folders, one for unit tests with all dependencies mocked and the other for complete system tests where no dependencies are mocked. Larger projects would most likely require more structure and test on different levels of integrations. The important part is what kind of tests we added to verify step 2-6 in our model for secure API:s, this is not at complete test suite for a real-world product API.

Step 1 - This is validated by the webserver and infrastructure protection we use, this is not something we represent as tests in our application repo. We write tests for the code we own, even if the system tests we added implicitly will test some parts of step 1, we rely on the tests that the Spring Boot team does (validating correct http request format etc).

Step 2 - Verify token, is done using system tests on a running instance of the API (integrated with a token service).

Step 3 - Verify that we get the expected permission set (transform the access token), is done using unit tests for the Permission service. Note that in a real world system this would often involve a database or external service, and then we would need integration tests as well.

Step 4 - Verify input data, is done using unit test for the ProductId domain type (together with unit test for the Products controller).

Step 5 - Verify access to the operation, is done using unit tests for the Product service (together with unit test for the Products controller).

Step 6 - Verify access to the data, is done using unit tests for the Product service (together with unit test for the Products controller).

Note that the unit tests for the Product service also verifies the correct audit logging in order to assert requirements for accountability.

Also note that the system tests we added can be run against the production environment as well. It is important to continuously monitor the actual production environment, do health checks and detect any security misconfiguration in that environment (for example a public API endpoint).

The tests are run with maven. Default the system-tests are excluded, since they require a running instance. 

Run all unit tests with

```
mvn clean install
```

Run all system tests with

```
mvn clean install -DallTests
```

Note that for system test the token service (at https://localhost:4000) and the API (at https://localhost:8443) needs to be started.

To start token-service, run the docker-compose command inside the token-service folder: 
```
docker-compose up -d
```
To start the application, run:
```
mvn spring-boot:run
```
