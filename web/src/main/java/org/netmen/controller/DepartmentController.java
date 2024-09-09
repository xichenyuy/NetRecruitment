package org.netmen.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.netmen.common.result.Result;
import org.netmen.dao.po.Department;
import org.netmen.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/department")
@Tag(name = "department")
public class DepartmentController {

    @Autowired
    private DepartmentService departmentService;

    //新增部门数据
    @PostMapping("/add")
    @Operation(summary = "添加部门", description = "输入部门信息")
    public Result<Object> departmentAdd(@RequestBody String name, @RequestBody Integer organizationId) {
        //首先判断是否已经存在该部门信息
        Department department = departmentService.findName(name);
        if(department==null)
        {
            departmentService.departmentAdd(name, organizationId);
            return Result.success();
        }
        else{
            return Result.error().message("该部门已经存在！");
        }

    }

    //查询部门
    @GetMapping("/check")
    @Operation(summary = "查询部门", description = "输入数据查询部门")
    public Result<Object> list(Integer pageNum, Integer pageSize) {
        Page<Department> page = new Page<>(pageNum, pageSize);
        Page<Department> rolePage = departmentService.page(page);
        return Result.success().data(rolePage);
    }


    //删除部门
    @DeleteMapping("/delete")
    @Operation(summary = "删除部门", description = "根据id信息删除部门信息")
    public Result<Department> departmentDeleteById(@RequestParam Integer departmentId) {
        departmentService.departmentDeleteById(departmentId);
        return Result.success();
    }


    //修改部门
    @PutMapping("/update")
    @Operation(summary = "修改部门", description = "输入部门信息")
    public Result<Department> updateDepartment(@RequestBody Integer departmentId, @RequestBody String name, @RequestBody Integer organizationId) {
        departmentService.updateById(departmentId, name, organizationId);
        return Result.success();
    }

}