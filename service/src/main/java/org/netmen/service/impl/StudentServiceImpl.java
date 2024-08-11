package org.netmen.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.netmen.dao.mapper.StudentMapper;
import org.netmen.dao.po.Student;
import org.netmen.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StudentServiceImpl extends ServiceImpl<StudentMapper, Student> implements StudentService {

    @Autowired
    private StudentMapper studentMapper;

    /**
     * 插入学生表
     */
    @Override
    public void addStudent(Student student) {
        studentMapper.insert(student);
    }


}
