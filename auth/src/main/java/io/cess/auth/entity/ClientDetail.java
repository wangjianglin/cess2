package io.cess.auth.entity;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.client.Jackson2ArrayOrStringDeserializer;
import org.springframework.security.oauth2.provider.client.JacksonArrayOrStringDeserializer;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.*;

@Entity(name = "oauth_client_detial")
public class ClientDetail {
    @Id
    @Column(name = "client_id",length = 256)
    private String clientId;

    @Column(name = "resource_ids",length = 256)
    private String resourceIds;

    @Column(name = "client_secret",length = 256)
    private String clientSecret;

    @Column(name = "scope",length = 256)
    private String scope;

    @Column(name = "authorized_grant_types",length = 256)
    private String grantTypes;

    @Column(name = "web_server_redirect_uri",length = 256)
    private String redirectUri;

    @Column(name = "authorities",length = 256)
    private String authorities;

    @Column(name = "access_token_validity")
    private Integer accessTokenValidity;

    @Column(name = "refresh_token_validity")
    private Integer refreshTokenValidity;

    //additional_information VARCHAR(4096),
    @Column(name = "autoapprove",length = 256)
    private String autoapprove;

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getResourceIds() {
        return resourceIds;
    }

    public void setResourceIds(String resourceIds) {
        this.resourceIds = resourceIds;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getGrantTypes() {
        return grantTypes;
    }

    public void setGrantTypes(String grantTypes) {
        this.grantTypes = grantTypes;
    }

    public String getRedirectUri() {
        return redirectUri;
    }

    public void setRedirectUri(String redirectUri) {
        this.redirectUri = redirectUri;
    }

    public String getAuthorities() {
        return authorities;
    }

    public void setAuthorities(String authorities) {
        this.authorities = authorities;
    }

    public Integer getAccessTokenValidity() {
        return accessTokenValidity;
    }

    public void setAccessTokenValidity(Integer accessTokenValidity) {
        this.accessTokenValidity = accessTokenValidity;
    }

    public Integer getRefreshTokenValidity() {
        return refreshTokenValidity;
    }

    public void setRefreshTokenValidity(Integer refreshTokenValidity) {
        this.refreshTokenValidity = refreshTokenValidity;
    }

    public String getAutoapprove() {
        return autoapprove;
    }

    public void setAutoapprove(String autoapprove) {
        this.autoapprove = autoapprove;
    }
//    autoapprove VARCHAR(256)

////    @org.codehaus.jackson.annotate.JsonProperty("client_id")
////    @com.fasterxml.jackson.annotation.JsonProperty("client_id")
//    private String clientId;
//
////    @org.codehaus.jackson.annotate.JsonProperty("client_secret")
////    @com.fasterxml.jackson.annotation.JsonProperty("client_secret")
//    private String clientSecret;
//
////    @org.codehaus.jackson.map.annotate.JsonDeserialize(using = JacksonArrayOrStringDeserializer.class)
////    @com.fasterxml.jackson.databind.annotation.JsonDeserialize(using = Jackson2ArrayOrStringDeserializer.class)
//    private Set<String> scope = Collections.emptySet();
//
////    @org.codehaus.jackson.annotate.JsonProperty("resource_ids")
////    @org.codehaus.jackson.map.annotate.JsonDeserialize(using = JacksonArrayOrStringDeserializer.class)
////    @com.fasterxml.jackson.annotation.JsonProperty("resource_ids")
////    @com.fasterxml.jackson.databind.annotation.JsonDeserialize(using = Jackson2ArrayOrStringDeserializer.class)
//    private Set<String> resourceIds = Collections.emptySet();
//
////    @org.codehaus.jackson.annotate.JsonProperty("authorized_grant_types")
////    @org.codehaus.jackson.map.annotate.JsonDeserialize(using = JacksonArrayOrStringDeserializer.class)
////    @com.fasterxml.jackson.annotation.JsonProperty("authorized_grant_types")
////    @com.fasterxml.jackson.databind.annotation.JsonDeserialize(using = Jackson2ArrayOrStringDeserializer.class)
//    private Set<String> authorizedGrantTypes = Collections.emptySet();
//
////    @org.codehaus.jackson.annotate.JsonProperty("redirect_uri")
////    @org.codehaus.jackson.map.annotate.JsonDeserialize(using = JacksonArrayOrStringDeserializer.class)
////    @com.fasterxml.jackson.annotation.JsonProperty("redirect_uri")
////    @com.fasterxml.jackson.databind.annotation.JsonDeserialize(using = Jackson2ArrayOrStringDeserializer.class)
//    private Set<String> registeredRedirectUris;
//
////    @org.codehaus.jackson.annotate.JsonProperty("autoapprove")
////    @org.codehaus.jackson.map.annotate.JsonDeserialize(using = JacksonArrayOrStringDeserializer.class)
////    @com.fasterxml.jackson.annotation.JsonProperty("autoapprove")
////    @com.fasterxml.jackson.databind.annotation.JsonDeserialize(using = Jackson2ArrayOrStringDeserializer.class)
//    private Set<String> autoApproveScopes;
//
//    private List<GrantedAuthority> authorities = Collections.emptyList();
//
//    @org.codehaus.jackson.annotate.JsonProperty("access_token_validity")
//    @com.fasterxml.jackson.annotation.JsonProperty("access_token_validity")
//    private Integer accessTokenValiditySeconds;
//
//    @org.codehaus.jackson.annotate.JsonProperty("refresh_token_validity")
//    @com.fasterxml.jackson.annotation.JsonProperty("refresh_token_validity")
//    private Integer refreshTokenValiditySeconds;
//
//    @org.codehaus.jackson.annotate.JsonIgnore
//    @com.fasterxml.jackson.annotation.JsonIgnore
//    private Map<String, Object> additionalInformation = new LinkedHashMap<String, Object>();
//
//
//
//    @org.codehaus.jackson.annotate.JsonIgnore
//    @com.fasterxml.jackson.annotation.JsonIgnore
//    public boolean isSecretRequired() {
//        return this.clientSecret != null;
//    }
//
//    @Override
//    public boolean isAutoApprove(String scope) {
//        if (autoApproveScopes == null) {
//            return false;
//        }
//        for (String auto : autoApproveScopes) {
//            if (auto.equals("true") || scope.matches(auto)) {
//                return true;
//            }
//        }
//        return false;
//    }
//
//    @org.codehaus.jackson.annotate.JsonIgnore
//    @com.fasterxml.jackson.annotation.JsonIgnore
//    public boolean isScoped() {
//        return this.scope != null && !this.scope.isEmpty();
//    }
//
//    @Override
//    public String getClientId() {
//        return clientId;
//    }
//
//    public void setClientId(String clientId) {
//        this.clientId = clientId;
//    }
//
//    @Override
//    public String getClientSecret() {
//        return clientSecret;
//    }
//
//    public void setClientSecret(String clientSecret) {
//        this.clientSecret = clientSecret;
//    }
//
//    @Override
//    public Set<String> getScope() {
//        return scope;
//    }
//
//    public void setScope(Set<String> scope) {
//        this.scope = scope;
//    }
//
//    @Override
//    public Set<String> getResourceIds() {
//        return resourceIds;
//    }
//
//    public void setResourceIds(Set<String> resourceIds) {
//        this.resourceIds = resourceIds;
//    }
//
//    @Override
//    public Set<String> getAuthorizedGrantTypes() {
//        return authorizedGrantTypes;
//    }
//
//    public void setAuthorizedGrantTypes(Set<String> authorizedGrantTypes) {
//        this.authorizedGrantTypes = authorizedGrantTypes;
//    }
//
//    public Set<String> getRegisteredRedirectUri() {
//        return registeredRedirectUris;
//    }
//
//    public void setRegisteredRedirectUri(Set<String> registeredRedirectUris) {
//        this.registeredRedirectUris = registeredRedirectUris;
//    }
//
//    public Set<String> getAutoApproveScopes() {
//        return autoApproveScopes;
//    }
//
//    public void setAutoApproveScopes(Set<String> autoApproveScopes) {
//        this.autoApproveScopes = autoApproveScopes;
//    }
//
//    @Override
//    public List<GrantedAuthority> getAuthorities() {
//        return authorities;
//    }
//
//    public void setAuthorities(List<GrantedAuthority> authorities) {
//        this.authorities = authorities;
//    }
//
//    @Override
//    public Integer getAccessTokenValiditySeconds() {
//        return accessTokenValiditySeconds;
//    }
//
//    public void setAccessTokenValiditySeconds(Integer accessTokenValiditySeconds) {
//        this.accessTokenValiditySeconds = accessTokenValiditySeconds;
//    }
//
//    @Override
//    public Integer getRefreshTokenValiditySeconds() {
//        return refreshTokenValiditySeconds;
//    }
//
//    public void setRefreshTokenValiditySeconds(Integer refreshTokenValiditySeconds) {
//        this.refreshTokenValiditySeconds = refreshTokenValiditySeconds;
//    }
//
//    @Override
//    public Map<String, Object> getAdditionalInformation() {
//        return additionalInformation;
//    }
//
//    public void setAdditionalInformation(Map<String, Object> additionalInformation) {
//        this.additionalInformation = additionalInformation;
//    }
}
