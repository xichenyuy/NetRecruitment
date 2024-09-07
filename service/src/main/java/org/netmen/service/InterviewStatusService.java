package org.netmen.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.netmen.dao.po.InterviewStatus;
import org.springframework.stereotype.Service;

@Service
public interface InterviewStatusService extends IService<InterviewStatus> {

    void updateDeleted(Integer id);

    void interviewFailed(Integer id);

    void interviewPass(Integer id);

    void adjustDepartmentSelect(Integer departmentId, Integer studentId);

    void falseTouchRejection(Integer id);

    void initialize(Integer id);
}
