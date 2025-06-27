package anota.ai.api.domain.contato;

import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ContatoRepository extends JpaRepository<Contato, Long> {
    Page<Contato> findAllByAtivo(int ativo, Pageable paginacao);

    @Modifying
    @Transactional
    @Query("UPDATE Contato c SET c.empresa = null WHERE c.empresa.cod_empresa = :codEmpresa")
    void desvincularEmpresa(@Param("codEmpresa") Long codEmpresa);
}
