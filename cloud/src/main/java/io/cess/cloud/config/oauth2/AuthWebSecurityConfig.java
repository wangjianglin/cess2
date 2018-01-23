package io.cess.cloud.config.oauth2;

import io.cess.cloud.oauth2.LoginUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.AbstractAuthenticationFilterConfigurer;
import org.springframework.security.config.annotation.web.configurers.FormLoginConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;

import java.lang.reflect.Field;

@Configuration
//@Order(-30)
public class AuthWebSecurityConfig  extends WebSecurityConfigurerAdapter {

    @Value("${io.cess.auth.resource-permit-all:false}")
    private boolean permitAll;


    @Value("${io.cess.auth.pub-url:#{null}}")
    private String pubUrl;

    @Autowired(required = false)
    private LoginUserDetailsService userDetailsService;

//    @Autowired(required = false)
//    private OAuth2Service oAuth2Service;

    @Override
    public void configure(WebSecurity web) throws Exception {
//        super.configure(web);
//        web.ignoring().antMatchers("/oauth/login");
    }

    @Override
    protected UserDetailsService userDetailsService() {
        if(userDetailsService != null){
            return userDetailsService;
        }
        return super.userDetailsService();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.csrf().disable();

        http.rememberMe().alwaysRemember(false);

        FormLoginConfigurer<HttpSecurity> formLoginConfigurer = http.formLogin();
        formLoginConfigurer.loginPage("/oauth/login")
                .loginProcessingUrl("/oauth/login")
                .successHandler(new OAuthSuccessHandler(pubUrl))
                .failureUrl("/oauth/login?error")
                .permitAll();

        Field field = AbstractAuthenticationFilterConfigurer.class.getDeclaredField("authenticationEntryPoint");//.get(formLoginConfigurer);
        field.setAccessible(true);
        LoginUrlAuthenticationEntryPoint authenticationEntryPoint = (LoginUrlAuthenticationEntryPoint) field.get(formLoginConfigurer);
        authenticationEntryPoint.setUseForward(true);

        if(permitAll) {
            http.authorizeRequests()
                    .anyRequest().permitAll();
        }else{
            http.authorizeRequests()
                    .anyRequest().authenticated();
        }
        http.authorizeRequests()
                .antMatchers("/", "/oauth/authorize", "/oauth/confirm_access")
                .permitAll();

    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

        if(userDetailsService != null) {
            auth.userDetailsService(userDetailsService);
//            CessDaoAuthenticationProvider authenticationProvider = new CessDaoAuthenticationProvider();
//            authenticationProvider.setUserDetailsService(userDetailsService);
//            authenticationProvider.setAuthService(oAuth2Service);
//            auth.authenticationProvider(authenticationProvider);
        }else{
            auth.inMemoryAuthentication()
                    .withUser("user")
                    .password("{noop}password")
                    .roles("*")
                    .authorities("*");
        }
    }

    @Override
    @Bean
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }
}