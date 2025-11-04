package anota.ai.api.controller.importacao;

import anota.ai.api.domain.exportacao.dto.DadosListagemExportacaoLog;
import anota.ai.api.domain.importacao.dto.DadosListagemImportacaoLog;
import anota.ai.api.domain.importacao.repository.ImportacaoLogRepository;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/importar")
@SecurityRequirement(name = "bearer-key")
public class ImportacaoController {

    @Autowired
    private ImportacaoLogRepository importacaoLogRepository;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<DadosListagemImportacaoLog>> consultar(
            @PageableDefault(size = 10, sort = {"codImportacaoLog"}, direction = Sort.Direction.DESC) Pageable paginacao
    ) {
        Page<DadosListagemImportacaoLog> exportacaoLogs = importacaoLogRepository.findAll(paginacao).map(DadosListagemImportacaoLog::new);
        return ResponseEntity.ok(exportacaoLogs);
    }
}
