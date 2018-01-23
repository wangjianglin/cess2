package io.cess.auth.service.impl;

import io.cess.auth.entity.Permission;
import io.cess.auth.entity.Role;
import io.cess.auth.repository.PermissionRepository;
import io.cess.auth.repository.RoleRepository;
import io.cess.auth.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Component
@Transactional
public class PermissionServiceImpl implements PermissionService {

    @Autowired
    private PermissionRepository permissionRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public Collection<? extends GrantedAuthority> findAuthorityByScopes(Collection<String> scopes) {

        Collection<Permission> permissions = permissionRepository.findByRoldCodeIn(scopes);

        List<GrantedAuthority> list = new ArrayList<>();

        if(permissions != null){
            for(Permission permission : permissions){
                list.add(new SimpleGrantedAuthority(permission.getCode()));
            }
        }

        return list;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorityByUserId(long userId) {

        Iterable<Role> roles = roleRepository.findByUserId(userId);

        List<GrantedAuthority> authorities = new ArrayList<>();

        if(roles != null){
            for(Role role : roles){
                authorities.add(new SimpleGrantedAuthority("ROLE_"+role.getCode()));
                Collection<Permission> permissions = role.getPermissions();
                if(permissions != null){
                    for(Permission permission : permissions){
                        authorities.add(new SimpleGrantedAuthority(permission.getCode()));
                    }
                }
            }
        }

        return authorities;
    }
}
