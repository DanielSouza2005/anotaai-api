package anota.ai.api.domain.foto;

import anota.ai.api.domain.contato.Contato;
import anota.ai.api.domain.usuario.Usuario;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FotoService {

    @Value("${upload.dir.usuario}")
    private String uploadDirUsuario;

    @Value("${upload.dir.contato}")
    private String uploadDirContato;

    public void salvarFoto(Usuario usuario, MultipartFile foto) throws IOException {
        if (foto != null && !foto.isEmpty()) {
            Path uploadPath = Paths.get(uploadDirUsuario).toAbsolutePath().normalize();
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

    public void salvarFoto(Contato contato, MultipartFile foto) throws IOException {
        if (foto != null && !foto.isEmpty()) {
            Path uploadPath = Paths.get(uploadDirContato).toAbsolutePath().normalize();
            Files.createDirectories(uploadPath);

            String fotoAnterior = contato.getFoto();
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

            contato.setFoto(caminhoNovo.toString());
        }
    }
}
