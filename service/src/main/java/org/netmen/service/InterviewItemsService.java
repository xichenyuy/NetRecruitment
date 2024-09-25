package org.netmen.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.netmen.dao.po.InterviewItems;

public interface InterviewItemsService extends IService<InterviewItems> {
    InterviewItems findName(String title);

    void interviewItemsAdd(String title, Integer maxScore, Integer minScore, Integer failedScore, Integer goodScore, String remark);

    void interviewItemsDelete(Integer id);

    void interviewItemsUpdate(Integer id, String title, Integer maxScore, Integer minScore, Integer failedScore, Integer goodScore, String remark);
}
