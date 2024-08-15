package org.netmen.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.netmen.dao.mapper.FirstMapper;
import org.netmen.dao.mapper.StudentMapper;
import org.netmen.dao.po.First;
import org.netmen.dao.po.Student;
import org.netmen.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class StudentServiceImpl extends ServiceImpl<StudentMapper, Student> implements StudentService {


    private final StudentMapper studentMapper;

    @Autowired
    private FirstMapper firstMapper;

    public StudentServiceImpl(StudentMapper studentMapper) {
        this.studentMapper = studentMapper;
    }

    /**
     * 模糊查询
     *
     * @param name
     * @param pageNo
     * @param pageSize
     * @return
     */
    @Override
    public List<Student> findStudentsByName(String name, Integer pageNo, Integer pageSize) {

        // 创建分页对象
        Page<Student> page = new Page<Student>(pageNo, pageSize);
        QueryWrapper<Student> queryWrapper = new QueryWrapper<Student>().like("name", name);
        return studentMapper.selectPage(page, queryWrapper).getRecords();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveStudent(Student student) {
        // 1. 添加student
        studentMapper.insert(student);
        First first = new First();
        first.setFirstId(student.getId());
        first.setDepartmentId(student.getFirstId());
        // 2.添加first
        firstMapper.insert(first);
    }


}
