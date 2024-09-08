package org.netmen.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.swagger.v3.oas.annotations.Operation;
import org.netmen.common.result.Result;
import org.netmen.dao.po.Assessment;
import org.netmen.service.AssessmentService;
import org.netmen.service.InterviewInterviewItemsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/assessment")
public class AssessmentController {
    @Autowired
    private AssessmentService assessmentService;
    @Autowired
    private InterviewInterviewItemsService interviewInterviewItemsService;

    @PostMapping
    @Operation(summary = "添加新的已经做好的计分项", description = "传入要加入的学生的学号、要记录的计分项、对应的分数和评价")
    public Result add(Integer studentId,Integer I2IItemsId,
                      Integer score,String account){
        Assessment assessment = assessmentService.findAssessmentBySId(studentId,I2IItemsId);
        if(assessment == null){
            Assessment newOne = new Assessment();
            newOne.setStudentId(studentId);
            newOne.setInterviewInterviewItemsId(I2IItemsId);
            newOne.setScore(score);
            newOne.setAccount(account);
            newOne.setDeleted(false);
            assessmentService.save(newOne);
            return Result.success().message("成功记录分数");
        }
        return Result.error().message("已存在此学生");
    }

    @GetMapping
    @Operation(summary = "找到这个学生的所有的计分项",description = "传入要找的学生的学号（也就是student_id）和要找的面试，会返回一个数组，包含这个学生的此场面试的每一个评分项")
    public Result getInterviewItems(Integer studentId,Integer interviewId,
                                    Integer pageNum,Integer pageSize){
        //先判断这个学生在不在
        List<Map<String,Object>> list = assessmentService.findAssessmentOfOneById(studentId,pageNum,pageSize);
        if(list.isEmpty()) return Result.error().message("没有此学生的记录");

        //再筛选出此场面试的面试项
        List<Map<String,Object>> res = new ArrayList<>();
        for(int i = 0;i< list.size();i++){
            Map<String,Object> map = list.get(i);
            int checkIndex = (int) map.get("interview_interview_items_id");
            //如果是要找的面试对应的面试项
            if(interviewInterviewItemsService.check(checkIndex,interviewId)) {
                res.add(map);
            }
        }

        return Result.success().message("此学生本场面试的评分项如下").data(res);
    }

    //批量删除计分项
    @DeleteMapping("/{id}")
    @Operation(summary = "删除一个计分项",description = "传入要修改的学生学号、待删除的计分项")
    public Result delete(@PathVariable Integer id) {
        // 检查是否存在
        boolean exists = assessmentService.getById(id) != null;
        if (!exists) {
            return Result.error().message("此计分项不存在");
        }
        // 执行删除操作
        boolean res = assessmentService.removeById(id);
        if (res) {
            return Result.success().message("删除成功");
        } else {
            return Result.error().message("删除失败！");
        }
    }

    //修改计分项
    //@PostMapping("/{id}")
    @PostMapping("/update")
    @Operation(summary = "修改一个学生的计分项",description = "传入要修改的学生的计分项、待修改的计分项内容")
    public Result updateAssessment(Integer id, Integer i2iItemsId, Integer score, String account) {
        LambdaQueryWrapper<Assessment> wrapper = new LambdaQueryWrapper<>();
        //wrapper.eq(Assessment::getId, id);
        Assessment assessment = assessmentService.getById(id);
        // 检验一下存不存在
        if (assessment == null) {
            return Result.error().message("此计分项不存在");
        }

        boolean flag = false;
        if (assessment.getInterviewInterviewItemsId() != i2iItemsId) {
            flag = true;
            assessment.setInterviewInterviewItemsId(i2iItemsId);
        }
        if (!Objects.equals(assessment.getScore(), score)) {
            flag = true;
            assessment.setScore(score);
        }
        if (!Objects.equals(assessment.getAccount(), account)) {
            flag = true;
            assessment.setAccount(account);
        }

        if (flag) {
            // 执行更新操作
            boolean updateResult = assessmentService.updateById(assessment);
            if (updateResult) {
                return Result.success().message("修改成功！");
            } else {
                return Result.error().message("修改失败！");
            }
        } else {
            return Result.error().message("传入新数据与先前数据一致");
        }
    }
}
