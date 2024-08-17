package com.coresaken.mcserverlist.data.response;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
@Builder
public class Response {
    HttpStatus status;
    String message;
    String redirect;
}
