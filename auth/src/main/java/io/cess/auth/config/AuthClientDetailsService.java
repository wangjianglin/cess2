package io.cess.auth.config;

import io.cess.auth.entity.ClientDetail;
import io.cess.auth.service.ClientService;
import io.cess.cloud.oauth2.OAuth2ClientDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;
import org.springframework.util.StringUtils;


public class AuthClientDetailsService implements OAuth2ClientDetailsService {

    @Autowired
    private ClientService clientService;

    @Override
    public ClientDetails loadClientByClientId(String clientId) throws ClientRegistrationException {
        ClientDetail clientDetail = clientService.loadClientByClientId(clientId);

        BaseClientDetails details = new BaseClientDetails(clientDetail.getClientId(), clientDetail.getResourceIds(), clientDetail.getScope(),
                clientDetail.getGrantTypes(), clientDetail.getAuthorities(), clientDetail.getRedirectUri());
        details.setClientSecret(clientDetail.getClientSecret());
        if (clientDetail.getAccessTokenValidity() != null) {
            details.setAccessTokenValiditySeconds(clientDetail.getRefreshTokenValidity());
        }
        if (clientDetail.getRefreshTokenValidity() != null) {
            details.setRefreshTokenValiditySeconds(clientDetail.getRefreshTokenValidity());
        }

        String scopes = clientDetail.getAutoapprove();
        if (scopes != null) {
            details.setAutoApproveScopes(StringUtils.commaDelimitedListToSet(scopes));
        }

        return details;
    }
}
