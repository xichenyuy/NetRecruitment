package org.netmen.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.netmen.dao.po.Assessment;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

public interface AssessmentService extends IService<Assessment> {
    Assessment findAssessmentBySId(Integer studentId, Integer i2IItemsId);

    List<Map<String, Object>> findAssessmentOfOneById(Integer studentId, int i, int i1);
}
