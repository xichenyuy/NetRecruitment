package org.netmen.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.netmen.dao.po.Student;
import org.springframework.stereotype.Service;

@Service
public interface StudentService extends IService<Student> {

    void addStudent(Student student);

}
