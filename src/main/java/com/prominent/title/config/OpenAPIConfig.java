package com.prominent.title.config;

import com.prominent.title.entity.document.Document;
import com.prominent.title.entity.document.DocumentBinaries;
import com.prominent.title.entity.email.EmailEvent;
import com.prominent.title.entity.email.EmailQueue;
import com.prominent.title.entity.email.SmtpAccount;
import com.prominent.title.entity.user.LoginHistory;
import com.prominent.title.entity.user.Organization;
import com.prominent.title.entity.user.Role;
import com.prominent.title.entity.user.UserRoles;
import io.swagger.v3.core.converter.ModelConverters;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.customizers.OpenApiCustomiser;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Configuration
public class OpenAPIConfig {
    @Bean
    public OpenAPI customizeOpenAPI() {

        final String securitySchemeName = "bearerAuth";
        return new OpenAPI()
                .addSecurityItem(new SecurityRequirement()
                        .addList(securitySchemeName))
                .components(new Components()
                        .addSecuritySchemes(securitySchemeName, new SecurityScheme()
                                .name(securitySchemeName)
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")));
    }

    @Bean
    public OpenApiCustomiser openApiCustomiser() {
        List<Type> classes = new ArrayList<>();

        classes.add(Document.class);
        classes.add(DocumentBinaries.class);
        classes.add(Role.class);
        classes.add(UserRoles.class);
        classes.add(Organization.class);
        classes.add(LoginHistory.class);
        classes.add(EmailEvent.class);
        classes.add(EmailQueue.class);
        classes.add(SmtpAccount.class);

        return openApi -> {
            Map<String, Schema> stringSchemaMap = openApi.getComponents().getSchemas();
            ModelConverters modelConverters = ModelConverters.getInstance();
            classes.forEach(aClass -> stringSchemaMap.putAll(modelConverters.read(aClass)));
            openApi.getComponents().getSchemas().putAll(stringSchemaMap);
        };
    }
}
