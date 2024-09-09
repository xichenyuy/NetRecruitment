package org.netmen.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.netmen.dao.mapper.DepartmentMapper;
import org.netmen.dao.po.Department;
import org.netmen.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class DepartmentServiceImpl extends ServiceImpl<DepartmentMapper, Department> implements DepartmentService {

    @Autowired
    private DepartmentMapper departmentMapper;

    @Override
    public void departmentAdd(String name, Integer organizationId){
        Department department = new Department();
        department.setName(name);
        department.setOrganizationId(organizationId);
        department.setDeleted(false);
        departmentMapper.insert(department);
    }


    @Override
    public void departmentDeleteById(Integer departmentId) {
        departmentMapper.deleteById(departmentId);
    }

    @Override
    public void updateById(Integer id, String name, Integer organizationId) {
        Department department = new Department();
        department.setId(id);
        department.setName(name);
        department.setOrganizationId(organizationId);
        departmentMapper.updateById(department);
    }

    @Override
    public Department findName(String name) {
        QueryWrapper<Department> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("name", name);
        return departmentMapper.selectOne(queryWrapper);
    }
}
