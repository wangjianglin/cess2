//package io.cess.shorturl.config;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.boot.context.properties.ConfigurationProperties;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.http.HttpMethod;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
//import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
//import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
//import org.springframework.security.oauth2.provider.token.RemoteTokenServices;
//import org.springframework.security.oauth2.provider.token.TokenStore;
//import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
//
//@Configuration
//@EnableResourceServer
//public class ResourceServerConfiguration extends ResourceServerConfigurerAdapter{
//
//    @Autowired(required = false)
//    private TokenStore tokenStore;
//
//    @Value("${io.cess.auth.resource-id:#{null}}")
//    private String resourceId;
//
//    @Value("${io.cess.auth.resource-permit-all:true}")
//    private boolean permitAll;
//
//    @Override
//    public void configure(HttpSecurity http) throws Exception {
//        if(permitAll) {
//            http.csrf().disable()
//                    .authorizeRequests()
//                    .antMatchers("/**").permitAll();
//        }else{
//            http.csrf().disable()
//                    .authorizeRequests()
//                    .antMatchers("/**").authenticated();
//        }
//    }
//
//
//    @Override
//    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
//        resources.resourceId(resourceId);
//        if(tokenStore != null){
//            resources.tokenStore(tokenStore);
//        }
//    }
//
//}
