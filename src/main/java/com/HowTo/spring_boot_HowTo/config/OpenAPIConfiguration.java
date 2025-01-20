package com.HowTo.spring_boot_HowTo.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;

@Configuration
public class OpenAPIConfiguration {
	
	//sets Open API informations
   @Bean
   public OpenAPI defineOpenApi() {
       Server server = new Server();
       server.setUrl("http://localhost:8080");
       server.setDescription("HowTo");

       Contact myContact = new Contact();
       myContact.setName("HowTo - Admin");
       myContact.setEmail("howto1128376@gmail.com");

       Info information = new Info()
               .title("HowTo API")
               .version("1.0")
               .description("This API shows the HowTo API and all their methods")
               .contact(myContact);
       return new OpenAPI().info(information).servers(List.of(server));
   }
}