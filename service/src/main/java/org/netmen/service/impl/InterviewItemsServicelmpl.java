package org.netmen.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.netmen.dao.mapper.InterviewItemsMapper;
import org.netmen.dao.po.InterviewItems;
import org.netmen.service.InterviewItemsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InterviewItemsServicelmpl extends ServiceImpl<InterviewItemsMapper, InterviewItems> implements InterviewItemsService {

    @Autowired
    private InterviewItemsMapper interviewItemsMapper;

    @Override
    public void interviewItemsAdd(String title, Integer maxScore, Integer minScore, Integer failedScore, Integer goodScore, String remark){
        InterviewItems interviewItems = new InterviewItems();
        interviewItems.setTitle(title);
        interviewItems.setMaxScore(maxScore);
        interviewItems.setMinScore(minScore);
        interviewItems.setFailedScore(failedScore);
        interviewItems.setGoodScore(goodScore);
        interviewItems.setRemark(remark);
        interviewItems.setDeleted(false);
        interviewItemsMapper.insert(interviewItems);
    }

    @Override
    public void interviewItemsDelete(Integer id){
        interviewItemsMapper.deleteById(id);
    }

    @Override
    public void interviewItemsUpdate(Integer id, String title, Integer maxScore, Integer minScore, Integer failedScore, Integer goodScore, String remark){
        InterviewItems interviewItems = new InterviewItems();
        interviewItems.setId(id);
        interviewItems.setTitle(title);
        interviewItems.setMaxScore(maxScore);
        interviewItems.setMinScore(minScore);
        interviewItems.setFailedScore(failedScore);
        interviewItems.setGoodScore(goodScore);
        interviewItems.setRemark(remark);
        interviewItemsMapper.updateById(interviewItems);
    }

    @Override
    public InterviewItems findName(String title){
        QueryWrapper wrapper = new QueryWrapper<>();
        wrapper.eq("title",title);
        return interviewItemsMapper.selectOne(wrapper);
    }

}
