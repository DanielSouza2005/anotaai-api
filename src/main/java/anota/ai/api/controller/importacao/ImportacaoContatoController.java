package anota.ai.api.controller.importacao;

import anota.ai.api.domain.importacao.model.ImportacaoLog;
import anota.ai.api.domain.importacao.service.ImportacaoContatoService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/importar/contatos")
@SecurityRequirement(name = "bearer-key")
public class ImportacaoContatoController {

    @Autowired
    private ImportacaoContatoService importacaoContatoService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ImportacaoLog> importar(@RequestParam("arquivo") MultipartFile arquivo) {
        importacaoContatoService.importar(arquivo);

        return ResponseEntity.accepted().build();
    }
}
