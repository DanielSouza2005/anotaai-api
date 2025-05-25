package anota.ai.api.domain.foto;

import anota.ai.api.domain.contato.Contato;
import anota.ai.api.domain.usuario.Usuario;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class FotoService {

    private final SupabaseStorageService storageService;

    public FotoService(SupabaseStorageService storageService) {
        this.storageService = storageService;
    }

    public void salvarFoto(Usuario usuario, MultipartFile foto) throws IOException {
        String url = storageService.uploadFile(foto, "usuarios");
        usuario.setFoto(url);
    }

    public void salvarFoto(Contato contato, MultipartFile foto) throws IOException {
        String url = storageService.uploadFile(foto, "contatos");
        contato.setFoto(url);
    }
}
