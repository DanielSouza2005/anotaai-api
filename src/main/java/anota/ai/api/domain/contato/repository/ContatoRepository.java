package anota.ai.api.domain.contato.repository;

import anota.ai.api.domain.contato.model.Contato;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ContatoRepository extends JpaRepository<Contato, Long> {

    @Query(value = """
            SELECT DISTINCT c.*
            FROM contato c
            LEFT JOIN empresa e ON (c.cod_empresa = e.cod_empresa)
            LEFT JOIN contato_email ce ON (ce.cod_contato = c.cod_contato)
            LEFT JOIN contato_telefone ct ON (ct.cod_contato = c.cod_contato)
            WHERE c.ativo = :ativo
            AND(
                :nome IS NULL
                OR unaccent(lower(c.nome)) LIKE unaccent(lower('%' || :nome || '%'))
            )
            AND (
                :cpf IS NULL
                OR unaccent(lower(c.cpf)) LIKE unaccent(lower('%' || :cpf || '%'))
            )
            AND (
                :cargo IS NULL
                OR unaccent(lower(c.cargo)) LIKE unaccent(lower('%' || :cargo || '%'))
            )
            AND (
                :departamento IS NULL
                OR unaccent(lower(c.departamento)) LIKE unaccent(lower('%' || :departamento || '%'))
            )
            AND (
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
                 :email IS NULL
                 OR unaccent(lower(ce.email)) LIKE unaccent(lower(CONCAT('%' || :email || '%')))
            )
            AND (
                 :telefone IS NULL
                 OR unaccent(lower(ct.telefone)) LIKE unaccent(lower(CONCAT('%' || :telefone || '%')))
            )
            """, nativeQuery = true)
    Page<Contato> buscarFiltrado(@Param("nome") String nome,
                                 @Param("cpf") String cpf,
                                 @Param("telefone") String telefone,
                                 @Param("email") String email,
                                 @Param("cargo") String cargo,
                                 @Param("departamento") String departamento,
                                 @Param("razao") String razao,
                                 @Param("fantasia") String fantasia,
                                 @Param("cnpj") String cnpj,
                                 @Param("ativo") int ativo,
                                 Pageable pageable);

    @EntityGraph(attributePaths = {"emails", "telefones", "empresa"})
    Page<Contato> findAllByAtivo(int ativo, Pageable paginacao);

    @Modifying
    @Transactional
    @Query("UPDATE Contato c SET c.empresa = null WHERE c.empresa.cod_empresa = :codEmpresa")
    void desvincularEmpresa(@Param("codEmpresa") Long codEmpresa);
}
