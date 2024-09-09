package org.netmen.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.netmen.common.result.Result;
import org.netmen.dao.po.DepartmentInterview;
import org.netmen.dao.po.Interview;
import org.netmen.service.InterviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/interview")
@Tag(name = "interview")
public class InterviewController {
    @Autowired
    private InterviewService interviewService;

    //新增面试数据
    @PostMapping("/add")
    @Operation(summary = "添加部门数据", description = "输入部门信息")
    public Result<Object> interviewAdd(@RequestBody String name, @RequestBody String remark){

        //查找是否已经存在name
        Interview interview = interviewService.findName(name);
        if(interview == null)
        {
            interviewService.interviewAdd(name,remark);
            return Result.success();
        }
        else{
            return Result.error().message("该面试信息已经存在 !");
        }

    }

    //查询面试数据
    @GetMapping("/cheak")
    @Operation(summary = "查询面试数据", description = "输入数据查询面试")
    public Result<Object> list(Integer pageNum, Integer pageSize) {
        Page<Interview> page = new Page<>(pageNum, pageSize);
        Page<Interview> rolePage = interviewService.page(page);
        return Result.success().data(rolePage);
    }

    //删除面试数据
    @DeleteMapping("/delete")
    @Operation(summary = "删除面试数据", description = "")
    public Result<Object> deleteById(@RequestParam Integer id){
        interviewService.interviewDelete(id);
        return Result.success();
    }


    //修改面试数据
    @PutMapping("/update")
    @Operation(summary = "修改面试数据", description = "")
    public Result<Object> departmentInterviewUpdate(@RequestBody Integer id,@RequestBody String name,@RequestBody String remark){
        interviewService.interviewUpdate(id,name,remark);
        return Result.success();
    }
}
