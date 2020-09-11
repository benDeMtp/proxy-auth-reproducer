package com.kawamind;

import io.quarkus.test.junit.QuarkusTest;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.junit.jupiter.api.*;
import org.wildfly.common.Assert;

import javax.inject.Inject;
import java.io.IOException;
import java.net.*;

@QuarkusTest
public class CountriesServiceTest {

    @Inject
    @RestClient
    CountriesService countriesService;

    @BeforeEach
     void setup(){

        Authenticator.setDefault(new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {

                if (System.getProperty("http.proxyHost")!=null && getRequestorType() == RequestorType.PROXY) {
                    String prot = getRequestingProtocol().toLowerCase();
                    String host = System.getProperty(prot + ".proxyHost", "");
                    String port = System.getProperty(prot + ".proxyPort", "0");
                    String user = System.getProperty(prot + ".proxyUser", "");
                    String password = System.getProperty(prot + ".proxyPassword", "");
                    if (getRequestingHost().equalsIgnoreCase(host)) {
                        if (Integer.parseInt(port) == getRequestingPort()) {
                            return new PasswordAuthentication(user, password.toCharArray());
                        }
                    }
                }
                return null;
            }
        });
    }

    @Test
    @DisplayName("getByName should return a country")
    void getByNameShouldReturnACountry() {
        Assert.assertNotNull(countriesService.getByName("greece"));
    }

    @Test
    @DisplayName("call api with java api should return status code 200")
    void callApiWithJavaApiShouldReturnStatusCode200() throws IOException {
        URL url = new URL("https://restcountries.eu/rest/v2/name/greece");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        Assertions.assertEquals(200,con.getResponseCode());
    }



}
