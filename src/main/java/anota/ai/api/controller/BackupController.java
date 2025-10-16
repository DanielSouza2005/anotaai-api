package anota.ai.api.controller;

import anota.ai.api.domain.backup.dto.DadosListagemBackup;
import anota.ai.api.domain.backup.repository.BackupRepository;
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
@RequestMapping("backup")
@SecurityRequirement(name = "bearer-key")
public class BackupController {

    @Autowired
    private BackupRepository repository;

    @GetMapping
    public ResponseEntity<Page<DadosListagemBackup>> listar(
            @PageableDefault(size = 10, sort = {"codBackup"}, direction = Sort.Direction.DESC) Pageable paginacao
    ) {
        Page<DadosListagemBackup> backups = repository.findAll(paginacao).map(DadosListagemBackup::new);
        return ResponseEntity.ok(backups);
    }

}
