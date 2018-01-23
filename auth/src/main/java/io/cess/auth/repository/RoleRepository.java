package io.cess.auth.repository;

import io.cess.auth.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;


//JpaRepository<CameraInfoPO, String>, JpaSpecificationExecutor<CameraInfoPO>
//@Repository
public interface RoleRepository extends JpaRepository<Role,Long>{

    //SELECT t FROM Teacher t join t.students s join s.books b where b.name = 'a'
    @Query("select obj from io.cess.auth.entity.Role obj join obj.users u where u.id = :id")
//    @Query("select obj from Role obj where obj.id = ?1")
    Set<Role> findByUserId(@Param("id") Long id);


//    @Query("select r from Role r")
//    List<Role> findByUserId();
}
