package com.bookstoreapi.bookstoreapi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.util.Map;

@RestController
@RequestMapping(value = "/api/media")
public class MediaController {
    //@Qualifier("filesystem") //llama al servicio implementado en repository StorageService
    @Autowired
    private StorageService storageService;

    @PostMapping(value = "/upload")
    Map<String, String> upload(@RequestParam("file") MultipartFile multipartFile) {
        String path = storageService.store(multipartFile);
        return Map.of("path", path);
    }

    @GetMapping(value = "/{filename}")
    ResponseEntity<Resource> getResource(@PathVariable(value = "filename") String filename) throws IOException {
        Resource resource = storageService.loadAsResource(filename);
        String contentType = Files.probeContentType(resource.getFile().toPath());
        return ResponseEntity
                .ok()
                .header(HttpHeaders.CONTENT_TYPE, contentType)
                .body(resource);
    }

}
