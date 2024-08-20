package org.netmen.dao.po;

import org.netmen.dao.mapper.StudentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
 class StudentTest {

    @Autowired
    private StudentMapper studentMapper;

    void testInsert() {
        Student student = new Student();
        student.setName("张三");
        studentMapper.insert(student);

    }

}