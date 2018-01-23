package io.cess.cloud.config.resourceServer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.authentication.BearerTokenExtractor;
import org.springframework.security.oauth2.provider.authentication.TokenExtractor;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@Configuration
@EnableResourceServer
@Order(-20)
public class ResourceServerConfiguration extends ResourceServerConfigurerAdapter{

    @Autowired(required = false)
    private TokenStore tokenStore;

    @Value("${io.cess.auth.resource-id:#{null}}")
    private String resourceId;

    @Value("#{'${io.cess.auth.resource-not-oauth-url:}'.trim().replaceAll(\"\\s*(?=,)|(?<=,)\\s*\", \"\").split(',')}")
    private List<String> notOAuthUrls;

    @Value("${io.cess.auth.resource-permit-all:false}")
    private boolean permitAll;

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();

        if(permitAll) {
            http.authorizeRequests()
                    .anyRequest().permitAll();
        }else{
            http.authorizeRequests()
                    .anyRequest()
                    .authenticated();
        }

        if(notOAuthUrls == null){
            notOAuthUrls = new ArrayList<>();
        }

        notOAuthUrls.add("/oauth/login");
        notOAuthUrls.add("/oauth/confirm_access");
        notOAuthUrls.add("/oauth/check_token");
        notOAuthUrls.add("/oauth/error");
        notOAuthUrls.add("/oauth/token");
        notOAuthUrls.add("/oauth/authorize");
        notOAuthUrls.add("/oauth/token_key");

        http.requestMatcher(new NotOAuthRequestMatcher(notOAuthUrls));

    }


    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        resources.resourceId(resourceId);
//        resources.authenticationEntryPoint()
        if(tokenStore != null){
            resources.tokenStore(tokenStore);
        }
    }


    class NotOAuthRequestMatcher implements RequestMatcher

    {

        private TokenExtractor tokenExtractor = new BearerTokenExtractor();

        private List<Pattern> notOAuthUrlPatterns = new ArrayList<>();

        public NotOAuthRequestMatcher(List<String> notOAuthUrls) {
            if(notOAuthUrls != null){
                for(String url : notOAuthUrls){
                    if(url == null || url.trim().length() == 0){
                        continue;
                    }
                    notOAuthUrlPatterns.add(Pattern.compile(url.trim()));
                }
            }
        }

        @Override
        public boolean matches(HttpServletRequest request) {

            if(tokenExtractor.extract(request) != null){
                return true;
            }

            String requestPath = getRequestPath(request);
            for (Pattern pattern : notOAuthUrlPatterns) {
                if (pattern.matcher(requestPath).matches()) {
                    return false;
                }
            }
            return true;
        }

        private String getRequestPath(HttpServletRequest request) {
            String url = request.getServletPath();

            if (request.getPathInfo() != null) {
                url += request.getPathInfo();
            }

            return url;
        }
    }
}
