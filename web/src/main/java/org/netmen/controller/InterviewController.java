package org.netmen.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.netmen.common.result.Result;
import org.netmen.dao.po.Interview;
import org.netmen.service.InterviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("interview")
@Validated
@Tag(name = "interview")
public class InterviewController {

    @Autowired
    private InterviewService interviewService;

    @PostMapping()
    @Operation(summary = "面试信息", description = "面试信息")
    public Result add(String name){
        Interview interview = new Interview();
        interview.setName(name);
        interviewService.save(interview);
        return Result.success();
    }


    @PutMapping("/{id}")
    @Operation(summary = "修改面试信息", description = "修改面试信息")
    public Result update(@PathVariable("id") Integer id,String  name){
        Interview byId = interviewService.getById(id);
        if(byId != null){
            return Result.error().message("ID不存在");
        }
        Interview interview = new Interview();
        interview.setInterviewId(id);
        interviewService.updateById(interview);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除面试信息", description = "删除面试信息")
    public Result delete(@PathVariable Integer id){
        Interview byId = interviewService.getById(id);
        if(byId != null){
            return Result.error().message("ID不存在");
        }
        interviewService.removeById(id);
        return Result.success();
    }

    @GetMapping("/{id}")
    @Operation(summary = "查询面试信息", description = "根据面试编号查询面试信息")
    public Result get(@PathVariable Integer id){
       return Result.success().data(interviewService.getById(id));
    }
}
