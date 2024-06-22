/**
 * Конфигуращия для OpenApi
 */
package org.example.orderservice.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Конфигурация OpenApi
 */
@Configuration
@OpenAPIDefinition(
    servers = {
        @io.swagger.v3.oas.annotations.servers.Server(url = "/", description = "Default Server URL")
    }
)
public class OpenApiConfig {
  @Bean
  public OpenAPI openApiDescription() {
    Contact contact = new Contact();
    contact.setName("Vladislav Bochkarev");
    contact.setEmail("vladiboc@gmail.com");
    contact.setUrl("https://github.com/vladiboc");

    License gplLicense = new License().name("GNU GPL v3").url("https://www.gnu.org/licenses/gpl-3.0.ru.html");

    Info apiInfo = new Info()
        .title("Сервис заказов через Kafka")
        .version("0.0.1")
        .contact(contact)
        .description("API сервиса заказов через Kafka")
        .termsOfService("https://https://github.com/vladiboc")
        .license(gplLicense);

    return new OpenAPI().info(apiInfo);
  }
}
