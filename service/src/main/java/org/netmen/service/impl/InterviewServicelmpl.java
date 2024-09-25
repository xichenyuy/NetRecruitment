package org.netmen.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.netmen.dao.mapper.InterviewMapper;
import org.netmen.dao.po.Interview;
import org.netmen.service.InterviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InterviewServicelmpl extends ServiceImpl<InterviewMapper,Interview> implements InterviewService {
    @Autowired
    private InterviewMapper interviewMapper;

    @Override
    public void interviewAdd(String name, String remark) {
        Interview interview = new Interview();
        interview.setRemark(remark);
        interview.setName(name);
        interview.setDeleted(false);
        interviewMapper.insert(interview);
    }

    @Override
    public void interviewDelete(Integer id){
        interviewMapper.deleteById(id);
    }

    @Override
    public void interviewUpdate(Integer id, String name, String remark){
        Interview interview = new Interview();
        interview.setId(id);
        interview.setName(name);
        interview.setRemark(remark);
        interviewMapper.updateById(interview);
    }

    @Override
    public Interview findName(String name){
        QueryWrapper<Interview> wrapper = new QueryWrapper<>();
        wrapper.eq("name",name);
        return interviewMapper.selectOne(wrapper);
    }


}
