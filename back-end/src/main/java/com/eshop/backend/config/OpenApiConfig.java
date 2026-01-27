package com.eshop.backend.config;

import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.Paths;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.parameters.RequestBody;
import io.swagger.v3.oas.models.responses.ApiResponse;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenApiCustomizer authEndpointsCustomizer() {
        return openApi -> {
            if (openApi.getPaths() == null) {
                openApi.setPaths(new Paths());
            }

            // -------- /login (form urlencoded) --------
            Schema<?> loginSchema = new Schema<>()
                    .type("object")
                    .addProperty("email", new Schema<>().type("string").example("shop1@demo.com"))
                    .addProperty("password", new Schema<>().type("string").format("password").example("shop1"))
                    .required(List.of("email", "password"));

            RequestBody loginBody = new RequestBody()
                    .required(true)
                    .content(new Content().addMediaType(
                            "application/x-www-form-urlencoded",
                            new MediaType().schema(loginSchema)
                    ));

            Operation loginOp = new Operation()
                    .addTagsItem("Auth")
                    .summary("Login (session cookie)")
                    .description("Authenticates via Spring Security form login. On success sets JSESSIONID cookie.")
                    .requestBody(loginBody)
                    .responses(new io.swagger.v3.oas.models.responses.ApiResponses()
                            .addApiResponse("200", new ApiResponse().description("Logged in (JSESSIONID set)"))
                            .addApiResponse("401", new ApiResponse().description("Invalid credentials")));

            openApi.getPaths().addPathItem("/api/login", new PathItem().post(loginOp));

            // -------- /logout --------
            Operation logoutOp = new Operation()
                    .addTagsItem("Auth")
                    .summary("Logout (invalidate session)")
                    .description("Logs out via Spring Security and invalidates the session.")
                    .responses(new io.swagger.v3.oas.models.responses.ApiResponses()
                            .addApiResponse("200", new ApiResponse().description("Logged out"))
                            .addApiResponse("401", new ApiResponse().description("Not authenticated")));

            openApi.getPaths().addPathItem("/api/logout", new PathItem().post(logoutOp));
        };
    }
}
