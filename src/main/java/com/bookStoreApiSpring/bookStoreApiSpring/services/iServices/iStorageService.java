package com.bookStoreApiSpring.bookStoreApiSpring.services.iServices;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;
import java.nio.file.Path;

public interface iStorageService {

    void init();

    String store(MultipartFile file);

    Path load(String filename);

    Resource loadAsResoure(String filename);

    void delete(String filename);

}
