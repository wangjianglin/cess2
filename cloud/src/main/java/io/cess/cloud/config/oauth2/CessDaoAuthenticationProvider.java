//package io.cess.cloud.config.oauth2;
//
//import io.cess.cloud.oauth2.OAuth2Service;
//import org.springframework.security.authentication.AuthenticationProvider;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;
//
//import java.util.Collection;
//
//
////public class CessDaoAuthenticationProvider implements AuthenticationProvider {
////
////    private OAuth2Service authService;
////
////
////    @Override
////    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
//////        return null;
//////    }
//////
//////    protected Authentication createSuccessAuthentication(Object principal, Authentication authentication, UserDetails user) {
////        UserDetails newUser = (UserDetails) authentication.getDetails();
////        UserDetails user = newUser;
////        if(authService != null){
////
////            newUser = new UserDetails() {
////                private Collection<? extends GrantedAuthority> authorities;
////                @Override
////                public Collection<? extends GrantedAuthority> getAuthorities() {
////                    if(authorities == null){
////                        authorities = authService.authorities(user.getUsername());
////                    }
////                    return authorities;
////                }
////
////                @Override
////                public String getPassword() {
////                    return user.getPassword();
////                }
////
////                @Override
////                public String getUsername() {
////                    return user.getUsername();
////                }
////
////                @Override
////                public boolean isAccountNonExpired() {
////                    return user.isAccountNonExpired();
////                }
////
////                @Override
////                public boolean isAccountNonLocked() {
////                    return user.isAccountNonLocked();
////                }
////
////                @Override
////                public boolean isCredentialsNonExpired() {
////                    return user.isCredentialsNonExpired();
////                }
////
////                @Override
////                public boolean isEnabled() {
////                    return user.isEnabled();
////                }
////            };
////        }
////
////        return authentication;
////    }
////
////    public OAuth2Service getAuthService() {
////        return authService;
////    }
////
////    public void setAuthService(OAuth2Service authService) {
////        this.authService = authService;
////    }
////
////
////    @Override
////    public boolean supports(Class<?> authentication) {
////        return (UsernamePasswordAuthenticationToken.class
////                .isAssignableFrom(authentication));
////    }
////}
//
//
//public class CessDaoAuthenticationProvider extends DaoAuthenticationProvider {
//
//    private OAuth2Service authService;
//
//    protected Authentication createSuccessAuthentication(Object principal, Authentication authentication, UserDetails user) {
//        UserDetails newUser = user;
//        if(authService != null){
//
//            newUser = new UserDetails() {
//                private Collection<? extends GrantedAuthority> authorities;
//                @Override
//                public Collection<? extends GrantedAuthority> getAuthorities() {
//                    if(authorities == null){
//                        authorities = authService.authorities(user.getUsername());
//                    }
//                    return authorities;
//                }
//
//                @Override
//                public String getPassword() {
//                    return user.getPassword();
//                }
//
//                @Override
//                public String getUsername() {
//                    return user.getUsername();
//                }
//
//                @Override
//                public boolean isAccountNonExpired() {
//                    return user.isAccountNonExpired();
//                }
//
//                @Override
//                public boolean isAccountNonLocked() {
//                    return user.isAccountNonLocked();
//                }
//
//                @Override
//                public boolean isCredentialsNonExpired() {
//                    return user.isCredentialsNonExpired();
//                }
//
//                @Override
//                public boolean isEnabled() {
//                    return user.isEnabled();
//                }
//            };
//        }
//        authentication = super.createSuccessAuthentication(principal, authentication, newUser);
//
//
//
//        return authentication;
//    }
//
//    public OAuth2Service getAuthService() {
//        return authService;
//    }
//
//    public void setAuthService(OAuth2Service authService) {
//        this.authService = authService;
//    }
//}