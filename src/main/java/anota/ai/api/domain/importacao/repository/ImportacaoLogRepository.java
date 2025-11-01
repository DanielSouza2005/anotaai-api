package anota.ai.api.domain.importacao.repository;

import anota.ai.api.domain.importacao.model.ImportacaoLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImportacaoLogRepository extends JpaRepository<ImportacaoLog, Long> {
}
