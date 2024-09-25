package org.netmen.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.netmen.common.result.Result;
import org.netmen.dao.po.Interview;
import org.netmen.dao.po.InterviewItems;
import org.netmen.service.InterviewItemsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/interviewItems")
@Tag(name = "interviewItems")
public class InterviewItemsController {
    @Autowired
    private InterviewItemsService interviewItemsService;

    //新增数据
    @PostMapping("/add")
    @Operation(summary = "添加部门数据", description = "输入部门信息")
    public Result<Object> interviewItemsAdd(@RequestBody String title, @RequestBody Integer maxScore, @RequestBody Integer minScore, @RequestBody Integer failedScore, @RequestBody Integer goodScore, @RequestBody String remark){

        //查找是否已经存在name
        InterviewItems interviewItems = interviewItemsService.findName(title);
        if(interviewItems == null)
        {
            interviewItemsService.interviewItemsAdd(title,maxScore,minScore,failedScore,goodScore,remark);
            return Result.success();
        }
        else{
            return Result.error().message("该面试信息已经存在 !");
        }

    }

    //查询数据
    @GetMapping("/cheak")
    @Operation(summary = "查询面试数据", description = "输入数据查询面试")
    public Result<Object> list(Integer pageNum, Integer pageSize) {
        Page<InterviewItems> page = new Page<>(pageNum, pageSize);
        Page<InterviewItems> rolePage = interviewItemsService.page(page);
        return Result.success().data(rolePage);
    }

    //删除数据
    @DeleteMapping("/delete")
    @Operation(summary = "删除面试数据", description = "")
    public Result<Object> deleteById(@RequestParam Integer id){
        interviewItemsService.interviewItemsDelete(id);
        return Result.success();
    }


    //修改面试数据
    @PutMapping("/update")
    @Operation(summary = "修改面试数据", description = "")
    public Result<Object> departmentInterviewUpdate(@RequestBody Integer id,@RequestBody String title, @RequestBody Integer maxScore, @RequestBody Integer minScore, @RequestBody Integer failedScore, @RequestBody Integer goodScore, @RequestBody String remark){
        interviewItemsService.interviewItemsUpdate(id,title,maxScore,minScore,failedScore,goodScore,remark);
        return Result.success();
    }
}
