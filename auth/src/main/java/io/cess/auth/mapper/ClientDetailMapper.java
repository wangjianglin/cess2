package io.cess.auth.mapper;

import io.cess.auth.entity.ClientDetail;
import org.apache.ibatis.annotations.*;

@Mapper
public interface ClientDetailMapper {

//    @Select("select count(1) from BC_QR_Scene")
//    int findCount();
//
//
//    @Select("select ID,Name,SceneCode from BC_QR_Scene where ID = #{id}")
//    QrCodeScene findById(@Param("id")int id);
    @Select("select * from oauth_client_detial where client_id = #{id}")
    @Results({
            @Result(property = "clientId",  column = "client_id", javaType = String.class),
            @Result(property = "clientSecret",  column = "client_secret", javaType = String.class)
    })
    ClientDetail findById(@Param("id") String id);
}
