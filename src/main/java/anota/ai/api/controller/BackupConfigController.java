package anota.ai.api.controller;

import anota.ai.api.domain.backup.dto.DadosAtualizacaoBackupConfig;
import anota.ai.api.domain.backup.dto.DadosListagemBackupConfig;
import anota.ai.api.domain.backup.dto.DadosListagemBackupLog;
import anota.ai.api.domain.backup.model.BackupConfig;
import anota.ai.api.domain.backup.repository.BackupConfigRepository;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("backupConfig")
@SecurityRequirement(name = "bearer-key")
public class BackupConfigController {

    @Autowired
    private BackupConfigRepository repository;

    @GetMapping
    public ResponseEntity<Page<DadosListagemBackupConfig>> listar(
            @PageableDefault(size = 10) Pageable paginacao
    ) {
        Page<DadosListagemBackupConfig> backupLogs = repository.findAll(paginacao).map(DadosListagemBackupConfig::new);
        return ResponseEntity.ok(backupLogs);
    }

    @PutMapping("/{cod_backupConfig}")
    public ResponseEntity<DadosListagemBackupConfig> atualizar(
            @PathVariable("cod_backupConfig") Long codBackupConfig,
            @RequestBody @Valid DadosAtualizacaoBackupConfig dados
    ) {
        BackupConfig backupConfig = repository.getReferenceById(codBackupConfig);

        backupConfig.atualizarDados(dados);
        repository.save(backupConfig);

        return ResponseEntity.ok(new DadosListagemBackupConfig(backupConfig));
    }
}
