package org.netmen.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.netmen.dao.mapper.DepartmentMapper;
import org.netmen.dao.po.Department;
import org.netmen.dao.po.Major;
import org.netmen.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DepartmentServiceImpl extends ServiceImpl<DepartmentMapper, Department> implements DepartmentService {

    @Autowired
    private DepartmentMapper departmentMapper;


    @Override
    public void departmentAdd(String name, Integer organizationId){
        Department department = new Department();
        department.setName(name);
        department.setOrganizationId(organizationId);
        departmentMapper.insert(department);
    }


    @Override
    public void deleteById(Integer departmentId) {
        departmentMapper.deleteById(departmentId);
    }

    public List<Department> findAll() {
        return departmentMapper.findAll();
    }


    @Override
    public void updateById(Integer departmentId, String name, Integer organizationId) {
        // 创建更新条件
        QueryWrapper<Department> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("department_id", departmentId);
        // 创建更新对象
        Department department = new Department();
        department.setName(name);
        department.setOrganizationId(organizationId);
        // 执行更新操作
        departmentMapper.update(department, queryWrapper);
    }

    @Override
    public Department findMajorByName(String name) {
        LambdaQueryWrapper<Department> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Department::getName,name);
        return departmentMapper.selectOne(wrapper);
    }


}
