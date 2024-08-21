package org.netmen.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.netmen.dao.po.Student;
import org.springframework.stereotype.Component;

@Mapper
public interface StudentMapper extends BaseMapper<Student> {
}
