package org.netmen.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.netmen.dao.po.InterviewStatus;
import org.netmen.dao.po.Student;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface StudentService extends IService<Student> {


    /**
     * 根据名字进行模糊拆查询
     * @param name
     * @param pageNo
     * @param pageSize
     * @return
     */
    List<Student> findStudentsByName(String name, Integer pageNo, Integer pageSize);


    void saveStudent(Student student, InterviewStatus interviewStatus);


    void deleteStudentByIds(List<Integer> ids);


    Student getByStudentId(String studentId);

    void updateStudentAndStatusAndRecordById(Student student, InterviewStatus interviewStatus);
}
