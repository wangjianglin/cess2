package io.cess.cloud.config.oauth2;

import io.cess.cloud.oauth2.LoginUserDetailsService;
import io.cess.cloud.oauth2.OAuth2ClientDetailsService;
import io.cess.cloud.oauth2.OAuth2Service;
import io.cess.cloud.oauth2.RefreshUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.security.oauth2.provider.token.AccessTokenConverter;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;

import javax.annotation.Resource;


@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfigurer extends AuthorizationServerConfigurerAdapter {

    @Autowired(required = false)
    private AuthenticationManager authenticationManager;

    @Autowired(required = false)
    private AccessTokenConverter accessTokenConverter;

    @Autowired(required = false)
    private OAuth2ClientDetailsService clientDetailsService;

    @Autowired(required = false)
    private RefreshUserDetailsService userDetailsService;

    @Value("${io.cess.auth.access-token-validity-seconds:7200}")
    private int accessTokenValiditySeconds;

    @Value("${io.cess.auth.refresh-token-validity-seconds:5184000}")
    private int refreshTokenValiditySeconds;

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {

        if(clientDetailsService != null) {
            clients.withClientDetails(new ClientDetailsService() {
                @Override
                public ClientDetails loadClientByClientId(String clientId) throws ClientRegistrationException {
                    return clientDetailsService.loadClientByClientId(clientId);
                }
            });
        }else{
            clients.inMemory()
                .withClient("demo")
                .secret("{noop}demo")
                .scopes("*")
                .authorities("*")
                .autoApprove(true)
                .authorizedGrantTypes("implicit", "refresh_token", "password", "authorization_code", "client_credentials");
        }

    }

    @Override
    public void configure(
            AuthorizationServerSecurityConfigurer oauthServer)
            throws Exception {
        oauthServer
                .tokenKeyAccess("permitAll()")
                .checkTokenAccess("isAuthenticated()");
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        if(accessTokenConverter != null){
            endpoints.accessTokenConverter(accessTokenConverter);
        }
        if(userDetailsService != null){
            endpoints.userDetailsService(userDetailsService);
        }
        if(authenticationManager != null) {
            endpoints.authenticationManager(authenticationManager);
        }

        DefaultTokenServices tokenServices = (DefaultTokenServices) endpoints.getDefaultAuthorizationServerTokenServices();

        tokenServices.setAccessTokenValiditySeconds(accessTokenValiditySeconds);
        tokenServices.setRefreshTokenValiditySeconds(refreshTokenValiditySeconds);

        if(clientDetailsService != null){
            tokenServices.setClientDetailsService(new ClientDetailsService() {
                @Override
                public ClientDetails loadClientByClientId(String clientId) throws ClientRegistrationException {
                    return clientDetailsService.loadClientByClientId(clientId);
                }
            });
        }
    }

}