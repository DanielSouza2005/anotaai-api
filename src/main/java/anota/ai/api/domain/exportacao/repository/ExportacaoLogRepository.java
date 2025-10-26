package anota.ai.api.domain.exportacao.repository;

import anota.ai.api.domain.exportacao.enums.TipoExportacao;
import anota.ai.api.domain.exportacao.model.ExportacaoLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExportacaoLogRepository extends JpaRepository<ExportacaoLog, Long> {
    Page<ExportacaoLog> findAllByTipo(TipoExportacao tipo, Pageable paginacao);
}

