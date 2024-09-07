package org.netmen.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.netmen.common.result.Result;
import org.netmen.dao.mapper.DepartmentInterviewMapper;
import org.netmen.dao.po.Department;
import org.netmen.dao.po.DepartmentInterview;
import org.netmen.service.DeapartmentInterviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.sql.Wrapper;

@RestController
@RequestMapping("/departmentInterview")
@Tag(name = "departmentInterview")
public class DepartmentInterviewController {

    @Autowired
    private DeapartmentInterviewService deapartmentInterviewService;

    //新增部门—面试数据
    @PostMapping("/add")
    @Operation(summary = "添加部门-组织数据", description = "输入部门-组织信息")
    public Result<Object> departmentInterviewAdd(@RequestBody Integer departmentId,@RequestBody Integer interviewId,@RequestBody Integer priority){
        //判断部门id和面试id是否存在
        DepartmentInterview departmentInterview = deapartmentInterviewService.findId(departmentId,interviewId);
        if(departmentInterview == null)
        {
            deapartmentInterviewService.departmentInterviewAdd(departmentId,interviewId,priority);
            return Result.success();
        }
        else{
            return Result.error().message("该部门和面试信息已经存在 !");
        }
    }

    //查询部门—面试数据
    @GetMapping("/cheak")
    @Operation(summary = "查询部门-面试数据", description = "输入数据查询部门-面试")
    public Result<Object> list(Integer pageNum, Integer pageSize) {
        Page<DepartmentInterview> page = new Page<>(pageNum, pageSize);
        Page<DepartmentInterview> rolePage = deapartmentInterviewService.page(page);
        return Result.success().data(rolePage);
    }

    //删除部门—面试数据
    @DeleteMapping("/delete")
    @Operation(summary = "删除部门-面试数据", description = "")
    public Result<Object> deleteById(@RequestParam Integer id){
        deapartmentInterviewService.departmentInterviewDelete(id);
        return Result.success();
    }


    //修改部门—面试数据
    @PutMapping("/update")
    @Operation(summary = "修改部门—面试数据", description = "")
    public Result<Object> departmentInterviewUpdate(@RequestBody Integer id,@RequestBody Integer departmentId,@RequestBody Integer interviewId,@RequestBody Integer priority){
        deapartmentInterviewService.departmentInterviewUpdate(id,departmentId,interviewId,priority);
        return Result.success();
    }
}
