package anota.ai.api.domain.backup.repository;

import anota.ai.api.domain.backup.model.BackupLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BackupLogRepository extends JpaRepository<BackupLog, Long> {
}
