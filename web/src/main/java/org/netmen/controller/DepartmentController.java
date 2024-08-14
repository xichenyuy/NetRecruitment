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
@Validated
@Tag(name = "department")
public class DepartmentController {

    @Autowired
    private DepartmentService departmentService;

    //新增部门数据
    @PostMapping("/add")
    @Operation(summary = "添加部门", description = "输入部门信息")
    public Result<Department> departmentAdd(@Valid @RequestBody String name, @Valid @RequestBody Integer organizationId) {
        departmentService.departmentAdd(name, organizationId);
        return Result.success();
    }


//    @GetMapping("/check")
//    @Operation(summary = "查询所有部门", description = "一次性查询所有部门")
//    public List<Department> getAllUsers() {
//        return departmentService.findAll();
//    }

    @GetMapping("/check")
    @Operation(summary = "查询所有部门", description = "一次性查询所有部门")
    public Result<Object> list(Integer pageNum, Integer pageSize) {
        Page<Department> page = new Page<>(pageNum, pageSize);
        Page<Department> rolePage = departmentService.page(page);
        return Result.success().data(rolePage);
    }



    @RequestMapping(value = "/delete", method = RequestMethod.PUT)
    @Operation(summary = "删除部门", description = "根据id信息删除部门信息")
    public Result<Department> deleteById(@PathVariable @RequestParam Integer departmentId) {
        departmentService.deleteById(departmentId);
        return Result.success();
    }



    @PutMapping("/update")
    @Operation(summary = "修改部门", description = "输入部门信息")
    public Result<Department> updateDepartment(@PathVariable @RequestParam Integer departmentId, @RequestParam String name, @RequestParam Integer organizationId) {
        departmentService.updateById(departmentId, name, organizationId);
        return Result.success();
    }

}