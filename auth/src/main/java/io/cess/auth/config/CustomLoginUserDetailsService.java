package io.cess.auth.config;

import io.cess.auth.entity.User;
import io.cess.auth.service.PermissionService;
import io.cess.auth.service.UserService;
import io.cess.cloud.oauth2.LoginUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CustomLoginUserDetailsService implements LoginUserDetailsService {
    @Autowired
    private UserService userService;
    @Autowired
    private PermissionService permissionService;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userService.loginUser(username);

        if(user == null){
            return null;
        }
        return new CessUserDetails(user,permissionService.getAuthorityByUserId(user.getId()));
    }
}
