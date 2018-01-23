package io.cess.cloud.oauth2;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.security.oauth2.provider.token.DefaultUserAuthenticationConverter;
import org.springframework.security.oauth2.provider.token.UserAuthenticationConverter;

import java.util.*;

import static io.cess.cloud.oauth2.AuthorityDecisionService.AUD;
import static io.cess.cloud.oauth2.AuthorityDecisionService.SCOPE;

public interface AuthorityDecisionService {

    String AUD = "aud";

    String CLIENT_ID = "client_id";

    String EXP = "exp";

    String JTI = "jti";

    String GRANT_TYPE = "grant_type";

    String ATI = "ati";

    String SCOPE = OAuth2AccessToken.SCOPE;

    String AUTHORITIES = "authorities";

    Map<String, ?> convertAccessToken(OAuth2AccessToken token, OAuth2Authentication authentication);

    OAuth2AccessToken extractAccessToken(String value, Map<String, ?> map);

    OAuth2Authentication extractAuthentication(Map<String, ?> map);

}
