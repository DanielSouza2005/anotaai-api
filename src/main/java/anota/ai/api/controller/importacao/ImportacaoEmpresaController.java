package anota.ai.api.controller.importacao;

import anota.ai.api.domain.importacao.model.ImportacaoLog;
import anota.ai.api.domain.importacao.service.ImportacaoEmpresaService;
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
@RequestMapping("/importar/empresas")
@SecurityRequirement(name = "bearer-key")
public class ImportacaoEmpresaController {

    @Autowired
    private ImportacaoEmpresaService importacaoEmpresaService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ImportacaoLog> importar(@RequestParam("arquivo") MultipartFile arquivo) {
        importacaoEmpresaService.importar(arquivo);

        return ResponseEntity.accepted().build();
    }
}
