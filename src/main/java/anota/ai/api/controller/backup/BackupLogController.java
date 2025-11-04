package anota.ai.api.controller.backup;

import anota.ai.api.domain.backup.dto.DadosListagemBackupLog;
import anota.ai.api.domain.backup.repository.BackupLogRepository;
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
@RequestMapping("backupLog")
@SecurityRequirement(name = "bearer-key")
public class BackupLogController {

    @Autowired
    private BackupLogRepository repository;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<DadosListagemBackupLog>> listar(
            @PageableDefault(size = 10, sort = {"codBackupLog"}, direction = Sort.Direction.DESC) Pageable paginacao
    ) {
        Page<DadosListagemBackupLog> backupLogs = repository.findAll(paginacao).map(DadosListagemBackupLog::new);
        return ResponseEntity.ok(backupLogs);
    }

}
