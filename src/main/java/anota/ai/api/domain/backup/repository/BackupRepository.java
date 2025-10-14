package anota.ai.api.domain.backup.repository;

import anota.ai.api.domain.backup.model.Backup;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface BackupRepository extends JpaRepository<Backup, Long> {
    List<Backup> findByDtInclusaoBefore(Date limite);
}
