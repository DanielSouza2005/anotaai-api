package anota.ai.api.infra.security;

import anota.ai.api.domain.usuario.Usuario;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenService {

    @Value("${api.security.token.secret}")
    private String secret;

    private static final String ISSUER = "Anota Aí";

    public String gerarToken(Usuario usuario) {
        try {
            var algoritmo = Algorithm.HMAC256(secret);

            return JWT.create()
                    .withIssuer(ISSUER)
                    .withSubject(usuario.getEmail())
                    .withClaim("nome", usuario.getNome())
                    .withClaim("foto", usuario.getFoto())
                    .withClaim("codigo", usuario.getCod_usuario())
                    .withExpiresAt(dataExpiracao())
                    .sign(algoritmo);
        } catch (JWTCreationException exception) {
            throw new RuntimeException("Erro ao gerar token JWT: ", exception);
        }
    }

    public String getSubject(String tokenJWT) {
        try {
            var algoritmo = Algorithm.HMAC256(secret);

            return JWT.require(algoritmo)
                    .withIssuer(ISSUER)
                    .build()
                    .verify(tokenJWT)
                    .getSubject();
        } catch (JWTVerificationException exception) {
            throw new RuntimeException("Token JWT Inválido ou Expirado!");
        }
    }

    public String getUserName(String tokenJWT) {
        try {
            var algoritmo = Algorithm.HMAC256(secret);

            return JWT.require(algoritmo)
                    .withIssuer(ISSUER)
                    .build()
                    .verify(tokenJWT)
                    .getClaim("nome")
                    .toString();
        } catch (JWTVerificationException exception) {
            throw new RuntimeException("Token JWT Inválido ou Expirado!");
        }
    }

    public String getCodigo(String tokenJWT) {
        try {
            var algoritmo = Algorithm.HMAC256(secret);

            return JWT.require(algoritmo)
                    .withIssuer(ISSUER)
                    .build()
                    .verify(tokenJWT)
                    .getClaim("codigo")
                    .toString();
        } catch (JWTVerificationException exception) {
            throw new RuntimeException("Token JWT Inválido ou Expirado!");
        }
    }

    private Instant dataExpiracao() {
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
    }
}
