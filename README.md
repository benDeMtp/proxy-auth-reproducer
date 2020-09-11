# proxy-auth-reproducer project

This project is a reproducer to proof that proxy authentication doesn't work quarkus-rest-client


This project consists of a simple service ( com.kawamind.CountriesService ) that call the same endpoint used in the official Quarkus documentation.

### Steps to reproduce :
#### Step one ( everything is fine)
Run 
```
mvn verify
```
Two tests are run (see CountriesServiceTest)

* getByNameShouldReturnACountry call the CountriesService

* callApiWithJavaApiShouldReturnStatusCode200 call the java API HttpUrlConnection

This two test must be OK

#### Step two (use a proxy)
First run a proxy with authentication.
We will use a docker image available on DockerHub
```
docker run --name squid_proxy -d \                                                                                                                                                                                                                     
  --restart=always \
  --publish 3128:3128 -p 2222:22 \
  -e SQUID_USER=test \
  -e SQUID_PASS=test \
  --volume /var/spool/squid \
thelebster/docker-squid-simple-proxy
```
Then we can restart the tests with proxy profile

```
mvn verify -Pproxy
``` 

* getByNameShouldReturnACountry -> KO with 407

* callApiWithJavaApiShouldReturnStatusCode200 -> OK

To make seconds test OK we used Authenticator class to setup  a PasswordAuthentication.
Resteasy doesn’t use this PasswordAuthentication and there is no known way to specify a user and a password.

