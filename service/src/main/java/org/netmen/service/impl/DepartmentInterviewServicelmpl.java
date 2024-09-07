package org.netmen.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.netmen.dao.mapper.DepartmentInterviewMapper;
import org.netmen.dao.po.DepartmentInterview;
import org.netmen.service.DeapartmentInterviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DepartmentInterviewServicelmpl extends ServiceImpl<DepartmentInterviewMapper, DepartmentInterview> implements DeapartmentInterviewService {

    @Autowired
    private DepartmentInterviewMapper departmentInterviewMapper;

    @Override
    public DepartmentInterview findId(Integer departmentId, Integer interviewId){
        QueryWrapper<DepartmentInterview> wrapper = new QueryWrapper<>();
        wrapper.eq("department_id",departmentId)
                .eq("interview_id",interviewId);
        return departmentInterviewMapper.selectOne(wrapper);
    }

    @Override
    public void departmentInterviewAdd(Integer departmentId, Integer interviewId, Integer priority){
        DepartmentInterview departmentInterview = new DepartmentInterview();
        departmentInterview.setDepartmentId(departmentId);
        departmentInterview.setInterviewId(interviewId);
        departmentInterview.setPriority(priority);
        departmentInterview.setDeleted(false);
        departmentInterviewMapper.insert(departmentInterview);
    }

    @Override
    public void departmentInterviewDelete(Integer id){
        departmentInterviewMapper.deleteById(id);
    }


    @Override
    public void departmentInterviewUpdate(Integer id,Integer departmentId, Integer interviewId, Integer priority){
        DepartmentInterview departmentInterview = new DepartmentInterview();
        departmentInterview.setId(id);
        departmentInterview.setDepartmentId(departmentId);
        departmentInterview.setInterviewId(interviewId);
        departmentInterview.setPriority(priority);
        departmentInterviewMapper.updateById(departmentInterview);
    }
}
