package com.coresaken.mcserverlist.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.net.MalformedURLException;


@RestController
@RequiredArgsConstructor
public class UploadsController {
    private final ResourceLoader resourceLoader;
    private final String uploadDir = System.getProperty("user.dir") + "/uploads/banners/";

    @GetMapping("/uploads/banners/{filename:.+}")
    public ResponseEntity<Resource> getFile(@PathVariable String filename) {
        File file = new File(uploadDir + filename);
        if (!file.exists() || file.isDirectory()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        Resource resource;
        try {
            resource = new UrlResource(file.toURI());
        } catch (MalformedURLException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

        String contentType = determineContentType(filename);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, contentType)
                .body(resource);
    }

    // Helper method to extract file extension
    private String getFileExtension(String filename) {
        int lastIndexOfDot = filename.lastIndexOf('.');
        if (lastIndexOfDot == -1) {
            return "";
        }
        return filename.substring(lastIndexOfDot);
    }

    // Helper method to determine the Content-Type based on file extension
    private String determineContentType(String filename) {
        String fileExtension = getFileExtension(filename).toLowerCase();
        switch (fileExtension) {
            case ".jpeg":
            case ".jpg":
                return MediaType.IMAGE_JPEG_VALUE;
            case ".png":
                return MediaType.IMAGE_PNG_VALUE;
            case ".gif":
                return MediaType.IMAGE_GIF_VALUE;
            default:
                return MediaType.APPLICATION_OCTET_STREAM_VALUE;
        }
    }
}
