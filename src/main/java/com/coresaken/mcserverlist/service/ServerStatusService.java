package com.coresaken.mcserverlist.service;

import com.coresaken.mcserverlist.data.dto.ServerStatusDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
public class ServerStatusService {

    final HttpClient client = HttpClient.newHttpClient();
    final ObjectMapper objectMapper = new ObjectMapper();

    public ServerStatusDto getServerStatus(String address) {
        String url = "https://api.mcstatus.io/v2/status/java/" + address;
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return objectMapper.readValue(response.body(), ServerStatusDto.class);
        } catch (HttpClientErrorException e) {
            return null;
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public ServerStatusDto getServerStatus(String ip, int port) {
        StringBuilder finalAddress = new StringBuilder();
        finalAddress.append(ip);
        if (port > 0) {
            finalAddress.append(":").append(port);
        }

        return getServerStatus(finalAddress.toString());
    }
}
