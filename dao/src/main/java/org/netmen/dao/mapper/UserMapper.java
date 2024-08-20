package org.netmen.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.netmen.dao.po.User;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}
