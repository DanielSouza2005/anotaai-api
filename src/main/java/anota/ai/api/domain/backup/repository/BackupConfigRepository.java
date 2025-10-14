package anota.ai.api.domain.backup.repository;

import anota.ai.api.domain.backup.model.BackupConfig;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BackupConfigRepository extends JpaRepository<BackupConfig, Long> {
    List<BackupConfig> findAllByAtivo(int ativo);
}
