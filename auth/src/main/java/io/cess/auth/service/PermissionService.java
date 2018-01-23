package io.cess.auth.service;

import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public interface PermissionService {
    Collection<? extends GrantedAuthority> findAuthorityByScopes(Collection<String> scopes);

    Collection<? extends GrantedAuthority> getAuthorityByUserId(long userId);
}
