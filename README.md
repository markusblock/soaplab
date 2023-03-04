# soaplab
Create and manage soape recipes and ingredients. A soap recipe calculator supports you with making the recipe production ready. It calculates the lye.

# Getting started
- [Java 17 JRE](https://adoptium.net/de/temurin/releases/?version=17) runtime is required

# Development
## Setting up IDE
- [Java 17 JDK](https://adoptium.net/de/temurin/releases/?version=17) development kit is required
- [Eclipse](https://www.eclipse.org/) Choosing your favorite IDE like eclipse or IntelliJ
- [lombok](https://projectlombok.org/) Install lombock plugin in your IDE
- [Docker](https://www.docker.com/) Docker can be used in tests to host a browser container

## Tests
- geckodriver needs to be installed
- Firefox will be used to execute the UI tests

## Start
Start the class class org.soaplab.SoaplabApplication

Because of a classloading issue with springboot devtools and microstream the restart feature of springboot devtools need to be disabled with VM argument "-Dspring.devtools.restart.enabled=false" at startup.

Data will be stored in the folder ${user.home}/microstream-soaplab


