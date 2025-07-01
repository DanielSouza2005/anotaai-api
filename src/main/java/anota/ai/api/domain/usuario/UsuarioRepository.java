package anota.ai.api.domain.usuario;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.userdetails.UserDetails;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    @Query(value = """
            SELECT * FROM usuario u
            WHERE u.ativo = :ativo
            AND (
                :nome IS NULL
                OR unaccent(lower(u.nome)) LIKE unaccent(lower('%' || :nome || '%'))
            )
            AND (
                :email IS NULL
                OR unaccent(lower(u.email)) LIKE unaccent(lower('%' || :email || '%'))
            )
            """, nativeQuery = true)
    Page<Usuario> buscarFiltrado(@Param("nome") String nome,
                                 @Param("email") String email,
                                 @Param("ativo") int ativo,
                                 Pageable pageable);

    Page<Usuario> findAllByAtivo(int ativo, Pageable paginacao);

    UserDetails findByEmail(String username);
}
