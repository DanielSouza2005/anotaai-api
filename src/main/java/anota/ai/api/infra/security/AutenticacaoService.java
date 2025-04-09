package anota.ai.api.infra.security;

import anota.ai.api.domain.usuario.Usuario;
import anota.ai.api.domain.usuario.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AutenticacaoService implements UserDetailsService {

    @Autowired
    private UsuarioRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        return repository.findByEmail(username);
        System.out.println("Buscando usuário por e-mail: " + username);
        UserDetails usuario = repository.findByEmail(username);
        System.out.println("Resultado da busca: " + usuario);

        return usuario;
    }
}
