package org.netmen.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.netmen.dao.po.DepartmentInterview;
import org.netmen.dao.po.Interview;

public interface InterviewService extends IService<Interview> {
    void interviewAdd(String name, String remark);

    void interviewDelete(Integer id);

    void interviewUpdate(Integer id, String name, String remark);

    Interview findName(String name);
}
