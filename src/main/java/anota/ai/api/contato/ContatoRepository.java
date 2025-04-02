package anota.ai.api.contato;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContatoRepository extends JpaRepository<Contato, Long> {
    Page<Contato> findAllByAtivo(int ativo, Pageable paginacao);
}
