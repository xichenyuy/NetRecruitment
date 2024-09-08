package org.netmen.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.netmen.dao.po.InterviewRecord;

import java.util.List;
import java.util.Map;

public interface InterviewRecordService extends IService<InterviewRecord> {

    List<Map<String, Object>> getInterviewRecordList(Integer departmentId, Integer pageNum, Integer pageSize);
}
