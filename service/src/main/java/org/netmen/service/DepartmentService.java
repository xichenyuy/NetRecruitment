package org.netmen.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.netmen.dao.po.Department;



public interface DepartmentService extends IService<Department> {


    void departmentAdd(String name, Integer organizationId);

    void updateById(Integer departmentId, String name, Integer organizationId);

    Department findName(String name);

    void departmentDeleteById(Integer departmentId);
}
