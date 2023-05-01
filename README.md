# soaplab
Create and manage soape recipes and ingredients. A soap recipe calculator supports you with making the recipe production ready. It calculates the lye.

# Getting started
- [Java 17 JRE](https://adoptium.net/de/temurin/releases/?version=17) runtime is required
- [Apache Maven](https://maven.apache.org/) to start from commandline

## Running locally
```
git clone https://github.com/markusblock/soaplab.git
cd soaplab
mvn spring-boot:run
```

Data will be stored in the folder ${user.home}/microstream-soaplab

### Application
- Web UI [http://localhost:8080/soaplab/ui](http://localhost:8080/soaplab/ui)
- REST API [http://localhost:8080/soaplab/rest](http://localhost:8080/soaplab/rest)

### REST API documentation
- Swagger UI [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)
- OpenAPI docs [http://localhost:8080/api-docs](http://localhost:8080/api-docs)

# Development
## Setting up IDE
- [Java 17 JDK](https://adoptium.net/de/temurin/releases/?version=17) development kit is required
- [Apache Maven](https://maven.apache.org/) As build tool when building or starting from commandline
- [Eclipse](https://www.eclipse.org) Choosing your favorite IDE like eclipse or IntelliJ
- [lombok](https://projectlombok.org) Install lombock plugin in your IDE
- [Docker](https://www.docker.com) Docker can be used in tests to host a browser container

## Tests
Tests can be parameterized with following parameters
- -DtestBrowser=firefox|chrome selecting the browser for testing. In combination with local this browser needs to be installed on your system
- -DtestEnvironment=local|container either using local browser or browser in docker container
- -DtestLocale=EN
- -Dheadless=true|false running in headless mode

### MAC OS with Colima as container runtime and testcontainers
Make sure that file /var/run/docker.sock exists. Otherwise create a link with 
- sudo ln -sf $HOME/.colima/default/docker.sock /var/run/docker.sock

For the IDE to be able to access the environment variables they need to be set with launchctl:
- launchctl setenv TESTCONTAINERS_DOCKER_SOCKET_OVERRIDE=/var/run/docker.sock
- launchctl setenv DOCKER_HOST "unix://${HOME}/.colima/docker.sock"
- launchctl setenv TESTCONTAINERS_RYUK_DISABLED true

## Start
Start the class class org.soaplab.SoaplabApplication


