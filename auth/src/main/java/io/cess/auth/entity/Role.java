package io.cess.auth.entity;

import javax.persistence.*;
import java.util.Collection;

/**
 * RBAC 2
 * 互斥角色 ：同一用户只能分配到一组互斥角色集合中至多一个角色，支持责任分离的原则。互斥角色是指各自权限互相制约的两个角色。对于这类角色一个用户在某一次活动中只能被分配其中的一个角色，不能同时获得两个角色的使用权。常举的例子：在审计活动中，一个角色不能同时被指派给会计角色和审计员角色。
 * 基数约束 ：一个角色被分配的用户数量受限；一个用户可拥有的角色数目受限；同样一个角色对应的访问权限数目也应受限，以控制高级权限在系统中的分配。例如公司的领导人有限的；
 * 先决条件角色 ：可以分配角色给用户仅当该用户已经是另一角色的成员；对应的可以分配访问权限给角色，仅当该角色已经拥有另一种访问权限。指要想获得较高的权限，要首先拥有低一级的权限。就像我们生活中，国家主席是从副主席中选举的一样。
 * 运行时互斥 ：例如，允许一个用户具有两个角色的成员资格，但在运行中不可同时激活这两个角色。
 *
 * Created by lin on 8/1/16.
 */
@Entity(name="auth_role")
//@NamedQuery(name="roles",query="select obj from io.cess.auth.entity.Role obj join obj.users u where u.id = :id")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(unique = true, nullable = false)
    private Long id;
    private String code;
    @Column(name="role_desc")
    private String desc;
    /**
     * 限制受权人数，0表示不限
     */
    private int constraints = 0;

    @ManyToMany(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    @JoinTable(name = "auth_role_permission")
    private Collection<Permission> permissions;

    @ManyToMany(fetch = FetchType.LAZY,cascade = CascadeType.ALL,mappedBy = "roles")
    private Collection<User> users;

    @ManyToOne()
    private Role base;

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

    public int getConstraints() {
        return constraints;
    }

    public void setConstraints(int constraints) {
        this.constraints = constraints;
    }

    public Role getBase() {
        return base;
    }

    public void setBase(Role base) {
        this.base = base;
    }

    public Collection<Permission> getPermissions() {
        return permissions;
    }

    public void setPermissions(Collection<Permission> permissions) {
        this.permissions = permissions;
    }

    public Collection<User> getUsers() {
        return users;
    }

    public void setUsers(Collection<User> users) {
        this.users = users;
    }
}
