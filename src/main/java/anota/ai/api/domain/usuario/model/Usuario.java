package anota.ai.api.domain.usuario.model;

import anota.ai.api.domain.enums.StatusAtivo;
import anota.ai.api.domain.usuario.dto.DadosAtualizacaoUsuario;
import anota.ai.api.domain.usuario.dto.DadosCadastroUsuario;
import anota.ai.api.domain.usuario.enums.Admin;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Collection;
import java.util.Date;
import java.util.List;

@Table(name = "usuario")
@Entity(name = "Usuario")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "cod_usuario")

public class Usuario implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cod_usuario;

    private String nome;
    private String senha;
    private String email;
    private int ativo;
    private Date dt_inclusao;
    private Date dt_alteracao;
    private String foto;

    private int admin;

    public Usuario(DadosCadastroUsuario dados, BCryptPasswordEncoder encoder) {
        this.nome = dados.nome();
        this.senha = encoder.encode(dados.senha());
        this.email = dados.email();
        this.foto = dados.foto();

        this.ativo = StatusAtivo.ATIVO.getCodigo();
        this.dt_inclusao = new Date();
        this.dt_alteracao = new Date();
        this.admin = dados.admin();
    }

    public void atualizarDados(DadosAtualizacaoUsuario dados, BCryptPasswordEncoder encoder) {
        this.dt_alteracao = new Date();

        if (dados.nome() != null) {
            this.nome = dados.nome();
        }

        if (dados.senha() != null) {
            this.senha = encoder.encode(dados.senha());
        }

        if (dados.email() != null) {
            this.email = dados.email();
        }

        if (dados.foto() != null) {
            this.foto = dados.foto();
        }

        this.admin = dados.admin();
    }

    public void excluir() {
        this.ativo = StatusAtivo.INATIVO.getCodigo();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (this.admin == Admin.VERDADEIRO.getCodigo()) {
            return List.of(new SimpleGrantedAuthority("ROLE_ADMIN"));
        }

        return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getPassword() {
        return senha;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.ativo == StatusAtivo.ATIVO.getCodigo();
    }
}
