package io.cess.cloud.oauth2;

import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientRegistrationException;

public interface OAuth2ClientDetailsService {

    ClientDetails loadClientByClientId(String clientId) throws ClientRegistrationException;

}
