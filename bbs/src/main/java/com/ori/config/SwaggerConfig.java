package com.ori.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
public class SwaggerConfig {
    @Bean
    public Docket customDocket() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.ori.controller"))
                .build();
    }

    private ApiInfo apiInfo() {
        Contact contact = new Contact("leeway", "https://github.com/jiushizheyang459?tab=repositories", "leewayhe@qq.com");
        return new ApiInfoBuilder()
                .title("汽车车友交流平台")
                .description("爱车人的聚居地")
                .contact(contact)
                .version("1.1.0")
                .build();
    }
}
