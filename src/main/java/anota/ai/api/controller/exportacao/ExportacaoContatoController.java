package anota.ai.api.controller.exportacao;

import anota.ai.api.domain.exportacao.dto.DadosListagemExportacaoLog;
import anota.ai.api.domain.exportacao.enums.TipoExportacao;
import anota.ai.api.domain.exportacao.model.ExportacaoLog;
import anota.ai.api.domain.exportacao.repository.ExportacaoLogRepository;
import anota.ai.api.domain.exportacao.service.ExportacaoContatoService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/exportar/contatos")
@SecurityRequirement(name = "bearer-key")
public class ExportacaoContatoController {

    @Autowired
    private ExportacaoContatoService exportacaoContatoService;

    @Autowired
    private ExportacaoLogRepository repository;

    @GetMapping
    public ResponseEntity<Page<DadosListagemExportacaoLog>> consultar(
            @PageableDefault(size = 10, sort = {"codExportacaoLog"}, direction = Sort.Direction.DESC) Pageable paginacao
    ) {
        Page<DadosListagemExportacaoLog> exportacaoLogs = repository.findAllByTipo(TipoExportacao.CONTATO, paginacao).map(DadosListagemExportacaoLog::new);
        return ResponseEntity.ok(exportacaoLogs);
    }

    @PostMapping
    public ResponseEntity<ExportacaoLog> exportarContatos() throws InterruptedException {
        ExportacaoLog exportacao = exportacaoContatoService.iniciarExportacao();
        return ResponseEntity.accepted().build();
    }

    @PostMapping("/cabecalho")
    public ResponseEntity<ExportacaoLog> exportarCabecalhoContatos() throws Exception {
        ExportacaoLog exportacao = exportacaoContatoService.iniciarExportacaoCabecalho();
        return ResponseEntity.ok().body(exportacao);
    }
}
