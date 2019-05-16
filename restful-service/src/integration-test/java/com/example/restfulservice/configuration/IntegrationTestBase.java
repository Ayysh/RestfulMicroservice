package com.example.restfulservice.configuration;

import io.restassured.RestAssured;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.BeforeSuite;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class IntegrationTestBase extends AbstractTestNGSpringContextTests {

    @BeforeSuite
    @SuppressWarnings("squid:S2696")
    public void setUpSuite() {
        String basePath = System.getProperty("server.base");
        RestAssured.basePath = (basePath == null || basePath.isEmpty()) ? "" : basePath;

        String baseHost = System.getProperty("server.host");
        RestAssured.baseURI = (baseHost == null || baseHost.isEmpty()) ? "http://localhost" : baseHost;
    }

    @LocalServerPort
    @SuppressWarnings("squid:S2696")
    public void setPort(int port) {
        RestAssured.port = port;
    }
}
