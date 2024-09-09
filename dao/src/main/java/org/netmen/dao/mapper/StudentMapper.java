package org.netmen.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.netmen.dao.po.Student;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

@Mapper
public interface StudentMapper extends BaseMapper<Student> {
    @Update("update test1.student set deleted = 1 where id = #{id}")
    void updateDeleted(Integer id);
    @Select("select * from test1.student where student_id = #{studentId}")
    Student getByStudentId(String studentId);
}
