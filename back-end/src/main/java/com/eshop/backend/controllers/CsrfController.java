package com.eshop.backend.controllers;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Auth")
@RestController
public class CsrfController {
    @GetMapping("/csrf")
    public ResponseEntity<Void> csrf(@Parameter(hidden = true) CsrfToken token) {
        return ResponseEntity.noContent()
                .header("Cache-Control", "no-store")
                .build();
    }
}