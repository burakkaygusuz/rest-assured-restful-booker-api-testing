# Rest Assured Restful-Booker API Testing

[![Rest Assured](https://img.shields.io/maven-central/v/io.rest-assured/kotlin-extensions?color=3CB371&label=rest-assured&logo=rest-assured&logoColor=3CB371&style=for-the-badge)](https://rest-assured.io/)

[Restful Booker API](https://restful-booker.herokuapp.com) testing using rest-assured.io.

## Prerequisites

Make sure you have installed and configured the environment variables all the following prerequisites on your
development machine:

| OS      | JDK                                  | Gradle                |
|---------|--------------------------------------|-----------------------|
| Windows | `scoop install java/temurin-lts-jdk` | `scoop install maven` |
| macOS   | `brew install --cask temurin`        | `brew install maven`  |

## Executing the Tests

- Clone the repository.

```shell
git clone git@github.com:burakkaygusuz/rest-assured-restful-booker-api-testing.git
```

- Change the directory.

```shell
cd rest-assured-restful-booker-api-testing
```

- Run the test.

```shell
mvn clean test
```

- Generate the report.

```shell
mvn allure:serve
```
