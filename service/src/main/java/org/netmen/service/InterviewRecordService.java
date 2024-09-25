package org.netmen.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.netmen.dao.po.InterviewRecord;
import org.netmen.dao.po.InterviewStatus;
import org.netmen.dao.po.Student;

import java.util.List;
import java.util.Map;

public interface InterviewRecordService extends IService<InterviewRecord> {

    List<Map<String, Object>> getInterviewRecordList(Integer departmentId, Integer pageNum, Integer pageSize);

    Map<String,Object> convertToMap(InterviewRecord interviewRecord);

    List<InterviewRecord> getStudentInterviewRecordList(Integer studentId);

    //初始化一个填了报名表的学生，这个接口应该在student处调用,用来插入第一条数据
    boolean initStudentInterviewRecord(Integer studentId, Integer currentDepartmentId);

    boolean adjustStudentInterviewRecord(Integer studentId, Integer adjustDepartmentId);

    Integer interviewPass(Integer studentId, Integer curDepartmentId);

    boolean interviewFailed(Integer studentId, Integer firstDepartmentId, Integer secondDepartmentId);

    boolean falseTouchRejection(Integer studentId,Integer firstDepartmentId, Integer secondDepartmentId);

    boolean initialize(Integer studentId);
}
