package com.bookStoreApiSpring.bookStoreApiSpring.controllers;
import com.bookStoreApiSpring.bookStoreApiSpring.services.iServices.iStorageService;
import lombok.AllArgsConstructor;
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
@AllArgsConstructor
public class MediaController {

    private final iStorageService storageService;

    @PostMapping(value = "/upload")
    public Map<String,String> upload(@RequestParam(value = "file") MultipartFile multipartFile){
        String path = storageService.store(multipartFile);
        return Map.of("path", path);
    }

    @GetMapping(value = "/{filename}")
    public ResponseEntity<Resource> getResource(@PathVariable(value = "filename") String filename) throws IOException {
        Resource resource = storageService.loadAsResoure(filename);
        String contenType = Files.probeContentType(resource.getFile().toPath());
        return ResponseEntity
                .ok()
                .header(HttpHeaders.CONTENT_TYPE, contenType)
                .body(resource);
    }
}
