package io.cess.auth.entity;


import javax.persistence.*;
import java.util.Collection;

/**
 * Created by lin on 8/1/16.
 */
@Entity(name="auth_permission")
public class Permission {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(unique = true, nullable = false)
    private Long id;
    private String code;

    @Column(name="permisssion_desc")
    private String desc;

    @ManyToMany(fetch = FetchType.LAZY,mappedBy = "permissions")
    private Collection<Scope> scopes;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Collection<Scope> getScopes() {
        return scopes;
    }

    public void setScopes(Collection<Scope> scopes) {
        this.scopes = scopes;
    }
}
