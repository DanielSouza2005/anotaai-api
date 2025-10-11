package anota.ai.api.domain.contato.repository;

import anota.ai.api.domain.contato.model.Contato;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ContatoRepository extends JpaRepository<Contato, Long> {

    @Query(value = """
            SELECT c.*,
                   e.razao,
                   e.fantasia,
                   e.cnpj
            FROM contato c
            LEFT JOIN empresa e ON (c.cod_empresa = e.cod_empresa)
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
                :celular IS NULL
                OR unaccent(lower(c.celular)) LIKE unaccent(lower('%' || :celular || '%'))
            )
            AND (
                    (
                        :telefone IS NULL
                        OR unaccent(lower(c.telefone)) LIKE unaccent(lower('%' || :telefone || '%'))
                    )
                    OR (
                        :telefone IS NULL
                        OR unaccent(lower(c.telefone2)) LIKE unaccent(lower('%' || :telefone || '%'))
                    )
            )
            AND (
                    (
                        :email IS NULL
                        OR unaccent(lower(c.email_pessoal)) LIKE unaccent(lower('%' || :email || '%'))
                    )
                    OR (
                        :email IS NULL
                        OR unaccent(lower(c.email_corp)) LIKE unaccent(lower('%' || :email || '%'))
                    )
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
            """, nativeQuery = true)
    Page<Contato> buscarFiltrado(@Param("nome") String nome,
                                 @Param("cpf") String cpf,
                                 @Param("celular") String celular,
                                 @Param("telefone") String telefone,
                                 @Param("email") String email,
                                 @Param("cargo") String cargo,
                                 @Param("departamento") String departamento,
                                 @Param("razao") String razao,
                                 @Param("fantasia") String fantasia,
                                 @Param("cnpj") String cnpj,
                                 @Param("ativo") int ativo,
                                 Pageable pageable);

    Page<Contato> findAllByAtivo(int ativo, Pageable paginacao);

    @Modifying
    @Transactional
    @Query("UPDATE Contato c SET c.empresa = null WHERE c.empresa.cod_empresa = :codEmpresa")
    void desvincularEmpresa(@Param("codEmpresa") Long codEmpresa);
}
