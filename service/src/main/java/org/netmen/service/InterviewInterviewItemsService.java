package org.netmen.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import org.netmen.dao.po.Assessment;
import org.netmen.dao.po.InterviewInterviewItems;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

public interface InterviewInterviewItemsService extends IService<InterviewInterviewItems> {
    boolean check(int checkIndex, Integer interviewId);

    List<Map<String, Object>> getList(Integer interviewId, Integer pageNum, Integer pageSize);
}
