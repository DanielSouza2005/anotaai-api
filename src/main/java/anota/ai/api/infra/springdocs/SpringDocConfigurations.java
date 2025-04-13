package anota.ai.api.infra.springdocs;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringDocConfigurations {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .components(new Components()
                        .addSecuritySchemes("bearer-key",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")))
                .info(new Info()
                        .title("Anota Aí API")
                        .description("RESTful API desenvolvida para o sistema de anotações e gerenciamento de contatos e empresas, com autenticação e boas práticas de desenvolvimento backend em Java.")
                        .contact(new Contact()
                                .name("Time Backend"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("http://anotaai/api/licenca")));
    }
}

