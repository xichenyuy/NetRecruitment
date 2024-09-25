package org.netmen.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.swagger.v3.oas.annotations.Operation;
import org.netmen.common.result.Result;
import org.netmen.dao.po.InterviewRecord;
import org.netmen.dao.po.InterviewStatus;
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

//    @PostMapping
//    @Operation(summary = "增加面试记录",description = "传入要添加面试者的部门id、要被加入面试的学生id、所加入的面试的级别（也就是第几轮面试）")
//    public Result add(Integer departmentId,Integer studentId,Integer priority){
//        return interviewRecordService.add(departmentId,studentId,priority);
//    }

//    @PostMapping("/{id}")
//    @Operation(summary = "更新此学生的面试轮次",description = "传入此学生的面试记录id,与要更换到的轮次")
//    public Result updateInterview(@PathVariable Integer id,Integer priority){
//        return interviewRecordService.updateInterview(id,priority);
//
//    }

//    @PostMapping("/eliminate/{id}")
//    @Operation(summary = "淘汰此学生",description = "传入此学生的面试记录id")
//    public Result eliminate(@PathVariable Integer id){
//        return interviewRecordService.eliminate(id);
//    }

    @GetMapping
    @Operation(summary = "部门面试记录",description = "传入想要获取信息的部门id，获取这个部门有关的所有面试记录")
    public Result getInterviewRecordList(Integer departmentId,Integer pageNum,Integer pageSize){
        List<Map<String,Object>> list = interviewRecordService.getInterviewRecordList(departmentId,pageNum,pageSize);
        if(list == null || list.isEmpty()){
            return Result.error().message("没有找到此部门的记录");
        }
        return Result.success().message("成功找到此部门的记录").data(list);
    }

    @GetMapping("/{studentId}")
    @Operation(summary = "学生面试记录",description = "传入学生学号id，获取这个学生的所有面试记录")
    public Result getStudentInterviewRecordList(@PathVariable Integer studentId){
        List<InterviewRecord> list = interviewRecordService.getStudentInterviewRecordList(studentId);
        List<Map<String,Object>> res = list.stream().map(interviewRecord -> {
            try{
                return interviewRecordService.convertToMap(interviewRecord);
            }catch (Exception e){
                throw new RuntimeException("InterviewRecord转换成Map出错:",e);
            }
        }).toList();
        if(list == null || list.isEmpty()){
            return Result.error().message("没有找到此学生的记录");
        }
        return Result.success().message("成功找到此学生的记录").data(res);
    }

//    @DeleteMapping("/{id}")
//    @Operation(summary = "删除此学生面试记录",description = "传入想要删除的面试记录id")
//    public Result deleted(@PathVariable Integer id){
//        return interviewRecordService.deleted(id);
//    }

    /**
     * 以下接口均为测试方法，可以不用看
     */
//    @PostMapping("/{studentId}")
//    @Operation(summary = "插入第一条数据")
//    public Result initStudentInterviewRecord(@PathVariable Integer studentId,Integer currentDepartmentId){
//        if(interviewRecordService.initStudentInterviewRecord(studentId,currentDepartmentId))
//            return Result.success();
//        return Result.error();
//    }
//
//    @PostMapping("/update/{studentId}")
//    @Operation(summary = "更新面试者的记录")
//    public Result interviewPass(@PathVariable Integer studentId, Integer curDepartmentId){
//        int res = interviewRecordService.interviewPass(studentId,curDepartmentId);
//        if(res == 1){
//            return Result.success().message("通过所有面试，待录取");
//        }else if(res == 0){
//            return Result.success().message("继续面试");
//        }
//        return Result.error().message("更新失败！");
//    }
//
//    @PostMapping("/back/{studentId}")
//    @Operation(summary = "回退")
//    public Result falseTouchRejection(@PathVariable Integer studentId,Integer firstDepartmentId, Integer secondDepartmentId){
//        boolean res = interviewRecordService.falseTouchRejection(studentId,firstDepartmentId,secondDepartmentId);
//        if(res){
//            return Result.success();
//        }
//        return Result.error();
//    }
//
//    @PostMapping("/fail/{studentId}")
//    @Operation(summary = "淘汰学生")
//    public Result interviewFailed(@PathVariable Integer studentId, Integer firstDepartmentId, Integer secondDepartmentId){
//        if(interviewRecordService.interviewFailed(studentId,firstDepartmentId,secondDepartmentId)){
//            return Result.success();
//        }
//        return Result.error();
//    }
//
//    @PostMapping("/initialize/{studentId}")
//    @Operation(summary = "初始化")
//    public Result initialize(@PathVariable Integer studentId){
//        if(interviewRecordService.initialize(studentId))
//            return Result.success();
//        return Result.error();
//    }
//
//   @PostMapping("/adjust/{studentId}")
//   @Operation(summary = "调剂")
//   public Result adjustStudentInterviewRecord(@PathVariable Integer studentId,Integer adjustDepartmentId){
//        if(interviewRecordService.adjustStudentInterviewRecord(studentId,adjustDepartmentId))
//            return Result.success();
//        return Result.error();
//   }

}
