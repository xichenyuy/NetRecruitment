package org.netmen.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.netmen.dao.mapper.InterviewRecordMapper;
import org.netmen.dao.po.InterviewRecord;
import org.netmen.service.InterviewRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class InterviewRecordServiceImpl extends ServiceImpl<InterviewRecordMapper, InterviewRecord> implements InterviewRecordService {
    @Autowired
    private InterviewRecordMapper interviewRecordMapper;

    @Override
    public List<Map<String, Object>> getInterviewRecordList(Integer departmentId, Integer pageNum, Integer pageSize) {
        LambdaQueryWrapper<InterviewRecord> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(InterviewRecord::getDepartmentId,departmentId);
        Page<InterviewRecord> page = new Page<>(pageNum,pageSize);
        IPage<InterviewRecord> ipage = interviewRecordMapper.selectPage(page,wrapper);

        if(ipage == null || ipage.getRecords().isEmpty()){
            return null;
        }

        List<Map<String,Object>> list = ipage.getRecords().stream().map(interviewRecord -> {
            try{
                return convertToMap(interviewRecord);
            }catch (Exception e){
                throw new RuntimeException("InterviewRecord转换成Map出错:",e);
            }
        }).collect(Collectors.toList());

        return list;
    }

    private Map<String,Object> convertToMap(InterviewRecord interviewRecord) {
        Map<String,Object> map = new HashMap<>();
        map.put("studentId",interviewRecord.getStudentId());
        map.put("departmentId",interviewRecord.getDepartmentId());
        map.put("failed",interviewRecord.getFailed());
        map.put("priority",interviewRecord.getPriority());
        return map;
    }
}
