package org.netmen.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.netmen.dao.po.Permission;

import java.util.List;

@Mapper
public interface PermissionMapper extends BaseMapper<Permission> {
    @Select("select distinct p.perms  from permission p \n" +
            "inner join role_permission rp on rp.permission_id = p.id \n" +
            "inner join `role` r on r.id = rp.role_id \n" +
            "inner join user_role ur on ur.role_id = r.id \n" +
            "inner join `user` u on u.id = ur.user_id \n" +
            "where u.id = #{userId}")
    List<String> getPermissionByUserId(Integer userId);

    @Select("select distinct perms from permission where !deleted")
    List<String> getSuperuserPermission();
}