package org.netmen.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.netmen.dao.mapper.InterviewInterviewItemsMapper;
import org.netmen.dao.po.InterviewInterviewItems;
import org.netmen.service.InterviewInterviewItemsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class InterviewInterviewItemsServiceImpl extends ServiceImpl<InterviewInterviewItemsMapper, InterviewInterviewItems> implements InterviewInterviewItemsService {
    @Autowired
    private InterviewInterviewItemsMapper interviewInterviewItemsMapper;

    @Override
    public boolean check(int checkIndex, Integer interviewId) {
        LambdaQueryWrapper<InterviewInterviewItems> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(InterviewInterviewItems::getId,checkIndex);
        List<InterviewInterviewItems> list = interviewInterviewItemsMapper.selectList(wrapper);
        return list.get(0).getInterviewId() == interviewId;
    }

    @Override
    public List<Map<String, Object>> getList(Integer interviewId, Integer pageNum, Integer pageSize) {
        Page<InterviewInterviewItems> page = new Page<>(pageNum,pageSize);
        LambdaQueryWrapper<InterviewInterviewItems> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(InterviewInterviewItems::getInterviewId,interviewId);
        IPage<InterviewInterviewItems> iPage = interviewInterviewItemsMapper.selectPage(page,wrapper);
        
        List<Map<String,Object>> list = iPage.getRecords().stream().map(interviewInterviewItems -> {
            try{
                return convertToMap(interviewInterviewItems);
            }catch (Exception e){
                throw new RuntimeException("转化失败",e);
            }
        }).toList();

        if(list == null||list.isEmpty())return null;

        return list;
    }

    private Map<String,Object> convertToMap(InterviewInterviewItems interviewInterviewItems) {
        Map<String,Object> map = new HashMap<>();
        map.put("id",interviewInterviewItems.getId());
        map.put("interview_id",interviewInterviewItems.getInterviewId());
        map.put("interview_items_id",interviewInterviewItems.getInterviewItemsId());
        return map;
    }
}
