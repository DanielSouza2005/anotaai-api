package anota.ai.api.domain.foto.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface StorageService {
    String uploadFile(MultipartFile file, String diretorio) throws IOException;
}
