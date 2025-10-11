package anota.ai.api.domain.foto.service;

import anota.ai.api.domain.contato.model.Contato;
import anota.ai.api.domain.foto.model.FotoTipo;
import anota.ai.api.domain.usuario.model.Usuario;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class FotoService {

    private final StorageService storageService;

    public FotoService(StorageService storageService) {
        this.storageService = storageService;
    }

    public void salvarFoto(Usuario usuario, MultipartFile foto) throws IOException {
        String url = storageService.uploadFile(foto, FotoTipo.USUARIOS.getPasta());
        usuario.setFoto(url);
    }

    public void salvarFoto(Contato contato, MultipartFile foto) throws IOException {
        String url = storageService.uploadFile(foto, FotoTipo.CONTATOS.getPasta());
        contato.setFoto(url);
    }
}
