package com.nhl4j;

import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.Objects;

public class BaseApiUnitTest {

    protected ResponseEntity<String> mockResponse(String mockResponseFilePath) {
        return ResponseEntity.ok(getFromFile(mockResponseFilePath));
    }

    protected String getFromFile(String fileName) {
        try {
            return new String(Objects.requireNonNull(
                    getClass().getClassLoader().getResourceAsStream(fileName)).readAllBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
