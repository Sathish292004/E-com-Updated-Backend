package Sathish292004.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI ecommerceAPI() {

        final String securitySchemeName = "Bearer Authentication";

        return new OpenAPI()

                .info(
                        new Info()
                                .title("SK E-Commerce REST API")
                                .version("1.0.0")
                                .description("Professional Spring Boot E-Commerce Backend")
                                .contact(
                                        new Contact()
                                                .name("Sathish Kumar")
                                                .url("https://github.com/Sathish292004")
                                )
                                .license(
                                        new License()
                                                .name("MIT License")
                                )
                )

                .addSecurityItem(
                        new SecurityRequirement()
                                .addList(securitySchemeName)
                )

                .components(
                        new Components()
                                .addSecuritySchemes(
                                        securitySchemeName,
                                        new SecurityScheme()
                                                .name(securitySchemeName)
                                                .type(SecurityScheme.Type.HTTP)
                                                .scheme("bearer")
                                                .bearerFormat("JWT")
                                )
                );
    }
}