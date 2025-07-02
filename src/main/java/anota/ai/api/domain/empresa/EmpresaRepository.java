package anota.ai.api.domain.empresa;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface EmpresaRepository extends JpaRepository<Empresa, Long> {

    @Query(value = """
            SELECT * FROM empresa e
            WHERE e.ativo = :ativo
            AND(
                :razao IS NULL
                OR unaccent(lower(e.razao)) LIKE unaccent(lower('%' || :razao || '%'))
            )
            AND (
                :fantasia IS NULL
                OR unaccent(lower(e.fantasia)) LIKE unaccent(lower('%' || :fantasia || '%'))
            )
            AND (
                :cnpj IS NULL
                OR unaccent(lower(e.cnpj)) LIKE unaccent(lower('%' || :cnpj || '%'))
            )
            AND (
                :ie IS NULL
                OR unaccent(lower(e.ie)) LIKE unaccent(lower('%' || :ie || '%'))
            )
            """, nativeQuery = true)
    Page<Empresa> buscarFiltrado(@Param("razao") String razao,
                                 @Param("fantasia") String fantasia,
                                 @Param("cnpj") String cnpj,
                                 @Param("ie") String ie,
                                 @Param("ativo") int ativo,
                                 Pageable pageable);

    Page<Empresa> findAllByAtivo(int ativo, Pageable paginacao);
}
