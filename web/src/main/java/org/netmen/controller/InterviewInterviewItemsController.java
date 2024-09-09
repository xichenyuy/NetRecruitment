package org.netmen.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.swagger.v3.oas.annotations.Operation;
import org.netmen.common.result.Result;
import org.netmen.dao.po.InterviewInterviewItems;
import org.netmen.service.InterviewInterviewItemsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("interview_interview_items")
public class InterviewInterviewItemsController {
    @Autowired
    private InterviewInterviewItemsService interviewInterviewItemsService;

    @PostMapping
    @Operation(summary = "将面试项加入面试中",description = "将面试项id和面试组id传入")
    public Result add(Integer interviewId,Integer interviewItemsId){
        LambdaQueryWrapper<InterviewInterviewItems> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(InterviewInterviewItems::getInterviewId,interviewId).eq(InterviewInterviewItems::getInterviewItemsId,interviewId);
        InterviewInterviewItems interviewInterviewItems = interviewInterviewItemsService.getOne(wrapper);

        if(interviewInterviewItems == null){
            InterviewInterviewItems newOne = new InterviewInterviewItems();
            newOne.setInterviewId(interviewId);
            newOne.setInterviewItemsId(interviewItemsId);
            newOne.setDeleted(false);
            boolean res = interviewInterviewItemsService.save(newOne);
            if(res){
                return Result.success().message("添加成功！");
            }
            return Result.error().message("添加失败！");
        }
        return Result.error().message("此面试中已经存在该面试项");
    }

    @GetMapping
    @Operation(summary = "找出某面试的所有面试项",description = "将要查找的面试的id传入")
    public Result getInterviewItemsList(Integer interviewId,Integer pageNum,Integer pageSize){
        List<Map<String,Object>> list = interviewInterviewItemsService.getList(interviewId,pageNum,pageSize);
        if(list == null ||list.isEmpty()){
            return Result.error().message("此面试中尚未有面试项！");
        }
        return Result.success().message("查找成功！").data(list);
    }

    @PostMapping("/{id}")
    @Operation(summary = "修改面试的面试项",description = "传入id，新的面试项id")
    public Result update(@PathVariable Integer id,Integer interviewItemsId){
        InterviewInterviewItems interviewInterviewItems = interviewInterviewItemsService.getById(id);
        if(interviewInterviewItems == null){
            return Result.error().message("此条面试-面试项不存在");
        }
        interviewInterviewItems.setInterviewItemsId(interviewItemsId);
        boolean res = interviewInterviewItemsService.updateById(interviewInterviewItems);
        if(!res){
            return Result.error().message("修改失败!");
        }
        return Result.success().message("修改成功");
    }

    @DeleteMapping
    @Operation(summary = "删去面试中的某项",description = "传入面试的id、面试项的id")
    public Result delete(Integer interviewId,Integer interviewItemsId){
        LambdaQueryWrapper<InterviewInterviewItems> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(InterviewInterviewItems::getInterviewId,interviewId).eq(InterviewInterviewItems::getInterviewItemsId,interviewItemsId);
        InterviewInterviewItems interviewInterviewItems = interviewInterviewItemsService.getOne(wrapper);
        if(interviewInterviewItems == null){
            return Result.error().message("此面试-面试项不存在");
        }
        boolean res = interviewInterviewItemsService.removeById(interviewInterviewItems.getId());
        if(!res) return Result.error().message("删除失败！");
        return Result.success().message("删除成功！");
    }
}
