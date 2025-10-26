package anota.ai.api.controller.exportacao;

import anota.ai.api.domain.exportacao.dto.DadosListagemExportacaoLog;
import anota.ai.api.domain.exportacao.repository.ExportacaoLogRepository;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/exportar")
@SecurityRequirement(name = "bearer-key")
public class ExportacaoController {

    @Autowired
    private ExportacaoLogRepository repository;

    @GetMapping
    public ResponseEntity<Page<DadosListagemExportacaoLog>> consultar(
            @PageableDefault(size = 10, sort = {"codExportacaoLog"}, direction = Sort.Direction.DESC) Pageable paginacao
    ) {
        Page<DadosListagemExportacaoLog> exportacaoLogs = repository.findAll(paginacao).map(DadosListagemExportacaoLog::new);
        return ResponseEntity.ok(exportacaoLogs);
    }
}
