package io.cess.auth.entity;

import javax.persistence.*;
import java.util.Collection;

/**
 * Created by lin on 8/1/16.
 */
@Entity(name="auth_user")
public class User {

    /**
     * 用于唯一的用户标识，所有业务数据通过id进行唯一标识
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(unique = true, nullable = false)
    private Long id;

    /**
     * 用户登录时的用户名，可更改
     */
    @Column(unique = true,length = 32)
    private String username;

    /**
     * 手机号，可以更换
     */
    @Column(unique = true,length = 20)
    private String mobile;

    /**
     * 邮箱
     */
    @Column(unique = true,length = 64)
    private String email;

    @Column(length = 64)
    private String password;


    /**
     * 用于生成 open id 时的加盐
     */
    @Column(length = 6)
    private String salt;

    @Column(name = "true_name",length = 64)
    private String trueName;

    @Column(name = "nick_name",length = 64)
    private String nickName;

    @Column(name = "head_portait",length = 256)
    private String headPortrait;

    @Column(length = 64)
    private String weixin;

    @Column(name = "weixin_openid",length = 256)
    private String weixinOpenId;

    private String qq;

    private String weibo;


    /**
     * 账号过期
     */
    @Column(name = "account_expired")
    private boolean accountExpired = false;

    /**
     * 账号锁定
     */
    @Column(name = "account_locked")
    private boolean accountLocked = false;

    /**
     * 认证过期
     */
    @Column(name = "credentials_expired")
    private boolean credentialsExpired = false;

    /**
     * 账号启用
     */
    @Column
    private boolean enabled = true;

    @ManyToMany(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    @JoinTable(name = "auth_user_role")
    private Collection<Role> roles;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getTrueName() {
        return trueName;
    }

    public void setTrueName(String trueName) {
        this.trueName = trueName;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getHeadPortrait() {
        return headPortrait;
    }

    public void setHeadPortrait(String headPortrait) {
        this.headPortrait = headPortrait;
    }

    public String getWeixin() {
        return weixin;
    }

    public void setWeixin(String weixin) {
        this.weixin = weixin;
    }

    public String getWeixinOpenId() {
        return weixinOpenId;
    }

    public void setWeixinOpenId(String weixinOpenId) {
        this.weixinOpenId = weixinOpenId;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public String getWeibo() {
        return weibo;
    }

    public void setWeibo(String weibo) {
        this.weibo = weibo;
    }

    public boolean isAccountExpired() {
        return accountExpired;
    }

    public void setAccountExpired(boolean accountExpired) {
        this.accountExpired = accountExpired;
    }

    public boolean isAccountLocked() {
        return accountLocked;
    }

    public void setAccountLocked(boolean accountLocked) {
        this.accountLocked = accountLocked;
    }

    public boolean isCredentialsExpired() {
        return credentialsExpired;
    }

    public void setCredentialsExpired(boolean credentialsExpired) {
        this.credentialsExpired = credentialsExpired;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Collection<Role> getRoles() {
        return roles;
    }

    public void setRoles(Collection<Role> roles) {
        this.roles = roles;
    }
}
