package com.bookStoreApiSpring.bookStoreApiSpring.services;
import com.bookStoreApiSpring.bookStoreApiSpring.services.iServices.iStorageService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.*;
import java.util.UUID;

@Service
public class FileSystemStorageService implements iStorageService {

    @Value("${app.storage.location}")
    private String storageLocation; ;

    @PostConstruct
    @Override
    public void init() {
        try {
            Files.createDirectories(Paths.get(storageLocation));
        } catch (IOException e) {
            throw new RuntimeException("No se puede inicializar la direcion de almacenamiento! ",e);
        }
    }

    @Override
    public String store(MultipartFile file) {
        String originFileName = file.getOriginalFilename();
        String filename = UUID.randomUUID()+"."+ StringUtils.getFilenameExtension(originFileName);

        if (file.isEmpty()){
            throw new RuntimeException("No se puede almacenar un archivo vacio!");
        }

        try {
            InputStream inputStream = file.getInputStream();
            Files.copy(inputStream, Paths.get(storageLocation).resolve(filename), StandardCopyOption.REPLACE_EXISTING);
        }catch (IOException e){
            throw new RuntimeException("Falla al almacenar el archivo!" + filename, e);
        }
        return filename;
    }

    @Override
    public Path load(String filename) {
        return Paths.get(storageLocation).resolve(filename);
    }

    @Override
    public Resource loadAsResoure(String filename) {

        try {
            Path file = load(filename);
            Resource resource = new UrlResource(file.toUri());

            if (resource.exists() || resource.isReadable()){
                return resource;
            }else {
                throw new RuntimeException("No se puede leer el archivo! "+filename);
            }
        }catch (MalformedURLException e){
            throw new RuntimeException("No se puede leer el archivo! "+filename, e);
        }
    }

    @Override
    public void delete(String filename) {
        Path file = load(filename);
        try {
            FileSystemUtils.deleteRecursively(file);
        }catch (IOException e){
            throw new RuntimeException("No se puede eliminar el archivo",e);
        }
    }
}
