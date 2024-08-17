package com.coresaken.mcserverlist.data.response;

import org.springframework.http.HttpStatus;

public record ErrorResponse(HttpStatus status, String message) {
}
