package io.cess.cloud.oauth2;

import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.Set;

public interface OAuth2Service {
    Collection<GrantedAuthority> authorityFilter(Collection<? extends GrantedAuthority> authorities, Set<String> scopes);
}
