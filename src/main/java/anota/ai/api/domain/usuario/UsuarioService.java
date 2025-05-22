package anota.ai.api.domain.usuario;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class UsuarioService {

    @Value("${upload.dir}")
    private String uploadDir;

    @Autowired
    private UsuarioRepository usuarioRepository;

    public void salvarFoto(Usuario usuario, MultipartFile foto) throws IOException {
        if (foto != null && !foto.isEmpty()) {
            Path uploadPath = Paths.get(uploadDir).toAbsolutePath().normalize();
            Files.createDirectories(uploadPath);

            String fotoAnterior = usuario.getFoto();
            if (fotoAnterior != null && !fotoAnterior.isBlank()) {
                Path caminhoAnterior = Paths.get(fotoAnterior).toAbsolutePath().normalize();

                try {
                    Files.deleteIfExists(caminhoAnterior);
                } catch (IOException e) {
                    System.err.println("Não foi possível excluir a foto anterior: " + caminhoAnterior);
                }
            }

            String novoNome = UUID.randomUUID() + "_" + foto.getOriginalFilename();
            Path caminhoNovo = uploadPath.resolve(novoNome);
            foto.transferTo(caminhoNovo.toFile());

            usuario.setFoto(caminhoNovo.toString());
        }
    }
}
