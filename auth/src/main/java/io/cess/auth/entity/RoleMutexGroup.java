package io.cess.auth.entity;

import javax.persistence.*;
import java.util.Collection;

/**
 * 组内的角色表示是不能同时授权给一个账户
 */
@Entity(name = "auth_role_mutex_group")
public class RoleMutexGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(unique = true, nullable = false)
    private Long id;

    @Column(name="group_desc")
    private String desc;

    @ManyToMany(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    @JoinTable(name = "auth_role_mutex_group_join")
    private Collection<Role> roles;
}
