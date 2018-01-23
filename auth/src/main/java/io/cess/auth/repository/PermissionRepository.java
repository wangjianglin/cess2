package io.cess.auth.repository;

import io.cess.auth.entity.Permission;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import java.util.Collection;

public interface PermissionRepository extends Repository<Permission, Long> {

    @Query("select obj from io.cess.auth.entity.Permission obj join obj.scopes scope where scope.code in :codes")
    Collection<Permission> findByRoldCodeIn(@Param("codes") Collection<String> codes);

}
