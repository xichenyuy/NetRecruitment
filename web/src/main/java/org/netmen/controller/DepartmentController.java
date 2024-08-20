package org.netmen.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.netmen.common.result.Result;
import org.netmen.dao.po.Department;
import org.netmen.dao.po.Major;
import org.netmen.dto.DepartmentDTO;
import org.netmen.service.DepartmentService;
import org.springframework.beans.BeanUtils;
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
    @Operation(summary = "增加部门",description = "要传入部门的名称，部门的id和组织id")
    public Result add(String name, Integer organizationId){
        Department department = departmentService.findMajorByName(name);
        //先检测一下是不是已经有这个专业了
        if(department==null){
            DepartmentDTO departmentDTO = new DepartmentDTO();
            Department newDepartment = new Department();
            departmentDTO.setName(name);
            departmentDTO.setOrganizationId(organizationId);
            BeanUtils.copyProperties(departmentDTO,newDepartment);
            boolean sus = departmentService.save(newDepartment);
            if(sus){
                return Result.success();
            }else{
                return Result.error().message("添加失败！");
            }
        }
        return Result.error().message("此部门已存在");
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