package com.coresaken.mcserverlist.data.response;

public record ApiResponse<T>(T data, ErrorResponse error) {
}
