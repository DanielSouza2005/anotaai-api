package anota.ai.api.domain.supabase.provider;

import anota.ai.api.domain.foto.service.StorageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Objects;
import java.util.UUID;

@Service
public class SupabaseStorageService implements StorageService {

    @Value("${supabase.url}")
    private String supabaseUrl;

    @Value("${supabase.key}")
    private String supabaseKey;

    @Value("${supabase.bucket}")
    private String supabaseBucket;

    private final RestTemplate restTemplate = new RestTemplate();

    public String uploadFile(MultipartFile file, String diretorio) throws IOException {
        if (file == null || file.isEmpty()) {
            return null;
        }

        String fileName = diretorio + "/" + UUID.randomUUID() + "_" + file.getOriginalFilename();
        String uploadUrl = supabaseUrl + "/storage/v1/object/" + supabaseBucket + "/" + fileName;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.valueOf(Objects.requireNonNull(file.getContentType())));
        headers.set("Authorization", "Bearer " + supabaseKey);
        headers.set("x-upsert", "true");

        HttpEntity<byte[]> entity = new HttpEntity<>(file.getBytes(), headers);
        ResponseEntity<String> response = restTemplate.exchange(uploadUrl, HttpMethod.PUT, entity, String.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            return supabaseUrl + "/storage/v1/object/public/" + supabaseBucket + "/" + fileName;
        } else {
            throw new IOException("Erro ao enviar arquivo para Supabase: " + response.getBody());
        }
    }

    public String uploadFile(File file, String diretorio) throws IOException {
        if (file == null || !file.exists()) {
            throw new IOException("Arquivo inválido ou inexistente: " + (file != null ? file.getAbsolutePath() : "null"));
        }

        String fileName = diretorio + "/" + UUID.randomUUID() + "_" + file.getName();
        String uploadUrl = supabaseUrl + "/storage/v1/object/" + supabaseBucket + "/" + fileName;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.set("Authorization", "Bearer " + supabaseKey);
        headers.set("x-upsert", "true");

        byte[] fileBytes = Files.readAllBytes(file.toPath());
        HttpEntity<byte[]> entity = new HttpEntity<>(fileBytes, headers);

        ResponseEntity<String> response = restTemplate.exchange(uploadUrl, HttpMethod.PUT, entity, String.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            return supabaseUrl + "/storage/v1/object/public/" + supabaseBucket + "/" + fileName;
        } else {
            throw new IOException("Erro ao enviar arquivo para Supabase: " + response.getBody());
        }
    }

    public void deleteFileByUrl(String fileUrl) throws IOException {
        String prefix = "/object/public/" + supabaseBucket + "/";
        int index = fileUrl.indexOf(prefix);
        if (index == -1) {
            throw new IOException("URL inválida para deletar arquivo: " + fileUrl);
        }
        String pathInBucket = fileUrl.substring(index + prefix.length());

        String deleteUrl = supabaseUrl + "/storage/v1/object/" + supabaseBucket + "/" + pathInBucket;

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + supabaseKey);

        HttpEntity<Void> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(deleteUrl, HttpMethod.DELETE, entity, String.class);

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new IOException("Erro ao excluir arquivo do Supabase: " + response.getBody());
        }
    }

}

