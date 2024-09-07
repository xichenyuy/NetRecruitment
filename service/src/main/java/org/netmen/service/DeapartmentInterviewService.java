package org.netmen.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.netmen.dao.po.DepartmentInterview;

public interface DeapartmentInterviewService extends IService<DepartmentInterview> {

    DepartmentInterview findId(Integer departmentId, Integer interviewId);

    void departmentInterviewAdd(Integer departmentId, Integer interviewId, Integer priority);

    void departmentInterviewDelete(Integer id);

    void departmentInterviewUpdate(Integer id, Integer departmentId, Integer interviewId, Integer priority);
}
