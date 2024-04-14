package com.example.hotelreservation;

import java.util.Collections;
import net.bytebuddy.asm.Advice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UseCase {
    private static final String BASE_URL_FORMAT = "http://localhost:%d%s";
    @Autowired
    protected TestRestTemplate restTemplate;

    @LocalServerPort
    protected int port;

    protected String prepareUrl(String resource) {
        return String.format(BASE_URL_FORMAT, port, resource);
    }
    protected <T> HttpEntity<Object> createBody(T body) {
        return new HttpEntity<>(body);
    }

}
