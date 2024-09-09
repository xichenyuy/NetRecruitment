package org.netmen.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.swagger.v3.oas.annotations.Operation;
import org.netmen.common.result.Result;
import org.netmen.dao.po.InterviewRecord;
import org.netmen.service.InterviewRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("interviewRecord")
public class InterviewRecordController {
    @Autowired
    private InterviewRecordService interviewRecordService;

    @PostMapping
    @Operation(summary = "增加面试记录",description = "传入要添加面试者的部门id、要被加入面试的学生id、所加入的面试的级别（也就是第几轮面试）")
    public Result add(Integer departmentId,Integer studentId,Integer priority){
        LambdaQueryWrapper<InterviewRecord> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(InterviewRecord::getDepartmentId,departmentId).eq(InterviewRecord::getStudentId,studentId);
        InterviewRecord interviewRecord = interviewRecordService.getOne(wrapper);

        //检验一下存不存在
        if(interviewRecord == null){

            InterviewRecord newOne = new InterviewRecord();
            newOne.setDepartmentId(departmentId);
            newOne.setStudentId(studentId);
            newOne.setFailed(false);
            newOne.setPriority(priority);
            newOne.setDeleted(false);
            interviewRecordService.save(newOne);
        }else if(interviewRecord.getPriority() == priority){
            return Result.error().message("此记录已经存在！");
        }else{
            return Result.error().message("与已经存在的记录有冲突");
        }

        return Result.success().message("记录增加成功");
    }

    @PostMapping("/{id}")
    @Operation(summary = "更新此学生的面试轮次",description = "传入此学生的面试记录id,与要更换到的轮次")
    public Result updateInterview(@PathVariable Integer id,Integer priority){
        InterviewRecord interviewRecord = interviewRecordService.getById(id);
        if(interviewRecord == null){
            return Result.error().message("不存在此学生的面试记录");
        }
        interviewRecord.setPriority(priority);
        boolean res = interviewRecordService.updateById(interviewRecord);
        if(!res){
            return Result.error().message("更新失败!");
        }
        return Result.success().message("更新成功!");
    }

    @PostMapping("/eliminate")
    @Operation(summary = "淘汰此学生",description = "传入此学生的面试记录id")
    public Result eliminate(Integer id){
        InterviewRecord interviewRecord = interviewRecordService.getById(id);
        if(interviewRecord == null){
            return Result.error().message("不存在此学生的面试记录");
        }
        interviewRecord.setFailed(true);
        boolean res = interviewRecordService.updateById(interviewRecord);
        if(!res){
            return Result.error().message("更新失败!");
        }
        return Result.success().message("更新成功!");
    }

    @GetMapping
    @Operation(summary = "部门面试记录",description = "传入想要获取信息的部门id，获取这个部门有关的所有面试记录")
    public Result getInterviewRecordList(Integer departmentId,Integer pageNum,Integer pageSize){
        List<Map<String,Object>> list = interviewRecordService.getInterviewRecordList(departmentId,pageNum,pageSize);
        if(list == null || list.isEmpty()){
            return Result.error().message("没有找到此部门的记录");
        }
        return Result.success().message("成功找到此部门的记录").data(list);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除此学生面试记录",description = "传入想要删除的面试记录id")
    public Result deleted(@PathVariable Integer id){
        InterviewRecord interviewRecord = interviewRecordService.getById(id);
        if(interviewRecord == null){
            return Result.error().message("此条记录不存在！");
        }
        boolean res = interviewRecordService.removeById(id);
        if(!res){
            return Result.error().message("删除失败！");
        }
        return Result.success().message("删除成功！");
    }
}
