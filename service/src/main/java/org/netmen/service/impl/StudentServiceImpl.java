package org.netmen.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.netmen.dao.mapper.FirstMapper;
import org.netmen.dao.mapper.SecondMapper;
import org.netmen.dao.mapper.StudentMapper;
import org.netmen.dao.po.First;
import org.netmen.dao.po.Second;
import org.netmen.dao.po.Student;
import org.netmen.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
public class StudentServiceImpl extends ServiceImpl<StudentMapper, Student> implements StudentService {


    @Autowired
    private StudentMapper studentMapper;

    @Autowired
    private FirstMapper firstMapper;

    @Autowired
    private SecondMapper secondMapper;


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

    /**
     * 新增学生，并添加第一第二志愿信息
     *
     * @param student
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveStudent(Student student) {
        // 1. 添加student
        studentMapper.insert(student);
        First first = new First();
        Second second = new Second();
        first.setFirstId(student.getId());
        first.setDepartmentId(student.getFirstId());
        second.setSecondId(student.getId());
        second.setDepartmentId(student.getSecondId());
        // 2.添加first
        firstMapper.insert(first);
        // 3.添加second
        secondMapper.insert(second);
    }

    /**
     * 删除学生，并删除第一第二志愿信息
     *
     * @param ids
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteByIds(List<Integer> ids) {
        firstMapper.deleteBatchIds(ids);
        secondMapper.deleteBatchIds(ids);
        studentMapper.deleteBatchIds(ids);
    }

    /**
     * 修改学生信息，并第一第二志愿是否更改，如果更改，自动更改
     *
     * @param student
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateStudent(Student student) {
        // 获得修改前的学生记录
        Student updateBefore = studentMapper.selectById(student.getId());
        // 第一志愿修改了
        First first = new First();
        Second second = new Second();
        if (student.getFirstId() != null) {
//            log.info("更改之前的firstId：{}", updateBefore.getFirstId());
            if (updateBefore.getFirstId() == null || !updateBefore.getFirstId().equals(student.getFirstId())) {
                first.setDepartmentId(student.getFirstId());
                first.setFirstId(student.getId());
                firstMapper.updateById(first);
            }
        } else {
            log.info("student表的firstId：{}", student.getFirstId());
            first.setDepartmentId(null);
            log.info("first表的firstId：{}", first.getFirstId());
            first.setFirstId(student.getId());
            firstMapper.updateById(first);
        }

        // 第二志愿修改了

        if (student.getSecondId() != null) {// 是否为空值
            if (updateBefore.getSecondId() == null || !updateBefore.getSecondId().equals(student.getSecondId())) { // 是否修改
                second.setDepartmentId(student.getSecondId());
                second.setSecondId(student.getId());
                secondMapper.updateById(second);
            }
        } else {
            second.setDepartmentId(null);
            second.setSecondId(student.getId());
            secondMapper.updateById(second);
        }

        // 修改学生信息
        studentMapper.updateById(student);
    }
}
