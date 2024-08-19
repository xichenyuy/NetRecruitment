package org.netmen.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.*;
import org.netmen.dao.po.Department;

import java.util.List;

@Mapper
public interface DepartmentMapper extends BaseMapper<Department> {
    @Select("SELECT * FROM department")
    List<Department> findAll();

    void deleteById(Integer departmentId);

    int updateById(Department department);


}

