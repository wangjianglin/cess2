package io.cess.auth.config;

import io.cess.auth.service.PermissionService;
import io.cess.cloud.oauth2.OAuth2Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

public class AuthService implements OAuth2Service {

    @Autowired
    private PermissionService permissionService;

    @Override
    public Collection<GrantedAuthority> authorityFilter(Collection<? extends GrantedAuthority> authorities, Set<String> scopes) {

        if(authorities == null || authorities.isEmpty()){
            return null;
        }
        Collection<? extends GrantedAuthority> scopeAuthority = null;

        scopeAuthority = permissionService.findAuthorityByScopes(scopes);

        List<GrantedAuthority> list = new ArrayList<>();

        if(scopeAuthority == null) {
            return null;
        }

        for(GrantedAuthority granted : authorities){
            if(granted == null || granted.getAuthority() == null){
                continue;
            }
            if(granted.getAuthority().startsWith("ROLE_")) {
                list.add(granted);
                continue;
            }
            for(GrantedAuthority authority : scopeAuthority){

                if(authority == null){
                    continue;
                }
                if(granted.getAuthority().equals(authority.getAuthority())){
                    list.add(granted);
                    break;
                }
            }
        }
        return list;
    }
}
