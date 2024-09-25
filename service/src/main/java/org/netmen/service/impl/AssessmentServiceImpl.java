package org.netmen.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.netmen.dao.mapper.AssessmentMapper;
import org.netmen.dao.po.Assessment;
import org.netmen.service.AssessmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class AssessmentServiceImpl extends ServiceImpl<AssessmentMapper,Assessment> implements AssessmentService {

    @Autowired
    private AssessmentMapper assessmentMapper;

    @Override
    public Assessment findAssessmentBySId(Integer studentId, Integer i2IItemsId) {
        if(studentId == null || i2IItemsId == null){
            throw new IllegalArgumentException("studentId and i2IItemsId must not be null");
        }
        LambdaQueryWrapper<Assessment> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Assessment::getStudentId,studentId).eq(Assessment::getInterviewInterviewItemsId,i2IItemsId);

        List<Assessment> list = assessmentMapper.selectList(wrapper);

        if(list.isEmpty())
        {
            return null;
        }
        return list.get(0);
    }

    @Override
    public List<Map<String, Object>> findAssessmentOfOneById(Integer studentId, int pageNum, int pageSize) {
        Page<Assessment> page = new Page<>(pageNum,pageSize);
        LambdaQueryWrapper<Assessment> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Assessment::getStudentId,studentId);
        IPage<Assessment> ipage = assessmentMapper.selectPage(page,wrapper);

        if (ipage == null || ipage.getRecords().isEmpty()) {
            return Collections.emptyList(); // 返回空列表而不是 null
        }
        List<Map<String,Object>> list = ipage.getRecords().stream().map(assessment -> {
            try{
                return convertToMap(assessment);
            } catch (Exception e) {
                throw new RuntimeException("Error converting assessment to map", e);
            }
        }).collect(Collectors.toList());

        return list;
    }

    private Map<String,Object> convertToMap(Assessment assessment) {
        HashMap<String,Object> map = new HashMap<>();
        map.put("id",assessment.getId());
        map.put("student_id",assessment.getStudentId());
        map.put("interview_interview_items_id",assessment.getInterviewInterviewItemsId());
        map.put("score",assessment.getScore());
        map.put("account",assessment.getAccount());
        return map;
    }
}
