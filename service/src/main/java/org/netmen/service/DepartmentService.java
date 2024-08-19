package org.netmen.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.netmen.dao.po.Department;

import java.util.List;


public interface DepartmentService extends IService<Department> {


    void departmentAdd(String name, Integer organizationId);

    List<Department> findAll();

    void deleteById(Integer departmentId);

    void updateById(Integer departmentId, String name, Integer organizationId);

}
