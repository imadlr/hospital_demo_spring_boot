package com.hopital.security.services;

import com.hopital.security.entities.AppRole;
import com.hopital.security.entities.AppUser;

public interface AccountService {
    AppUser addUser(String username, String password, String email, String confirmPassword);

    AppRole addRole(String role);

    void addRoleToUser(String username, String role);

    void removeRoleFromUser(String username, String role);

    AppUser loadUserByUsername(String username);
}
