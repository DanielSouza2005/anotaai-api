package anota.ai.api.usuario;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Page<Usuario> findAllByAtivo(int ativo, Pageable paginacao);
}
