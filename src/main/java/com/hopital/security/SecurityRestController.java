package com.hopital.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SecurityRestController {

   /* @GetMapping("/profile")
    public Authentication authentication(Authentication authentication) {
        return authentication;
    }*/

    /*@GetMapping("/profile") // Authentication implements l' interface Principal
    public Principal authentication(Principal principal) {
        return principal;
    }*/

    @GetMapping("/profile")
    public Authentication authentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

}
