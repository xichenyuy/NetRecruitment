package org.netmen.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.netmen.common.result.Result;
import org.netmen.dao.po.InterviewStatus;
import org.netmen.service.InterviewStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.DefaultAuthenticationEventPublisher;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@Tag(name = "interviewStatus")
@RequestMapping("/interviewStatus")
public class InterviewStatusController {

    @Autowired
    private InterviewStatusService interviewStatusService;

    /**
     * 根据id查看面试状态
     */
    @GetMapping("/{id}")
    @Operation(summary = "根据id查看面试状态接口", description = "根据id查看面试状态")
    public Result getInterviewStatusById(@PathVariable("id") Integer id) {
        try {
            InterviewStatus interviewStatus = interviewStatusService.getById(id);
            return Result.success().data(interviewStatus);
        } catch (Exception e) {
            return Result.error().message("查询失败");
        }

    }

    /**
     * 面试不通过
     */
    @PutMapping("/interviewFailed/{id}")
    @Operation(summary = "面试淘汰接口", description = "修改面试状态为未通过")
    public Result interviewFailed(@PathVariable("id") Integer id) {
        try {
            interviewStatusService.interviewFailed(id);
            return Result.success().message("淘汰成功");
        } catch (Exception e) {
            return Result.error().message("淘汰失败");
        }

    }

    /**
     * 面试通过
     */
    @PutMapping("/interviewPass/{id}")
    @Operation(summary = "面试通过接口", description = "修改面试状态为通过")
    public Result interviewPass(@PathVariable("id") Integer id) {
        try {
            interviewStatusService.interviewPass(id);
            return Result.success().message("通过成功");
        } catch (Exception e) {
            return Result.error().message("通过失败");
        }

    }

    /**
     * 调剂部门挑选接口
     */
    @PutMapping("/adjustDepartment")
    @Operation(summary = "部门调剂挑选接口", description = "传入部门id和学生id，调剂学生到该部门")
    public Result adjustDepartmentSelect(@RequestParam Integer departmentId,
                                         @RequestParam Integer studentId) {
        try {
            interviewStatusService.adjustDepartmentSelect(departmentId, studentId);
            return Result.success().message("调剂成功");
        } catch (Exception e) {
            return Result.error().message("调剂失败");
        }

    }

    /**
     * 防误触接口
     */
    @PutMapping("/falseTouchRejection/{id}")
    @Operation(summary = "防误触接口（有权限）", description = "修改面试状态为未通过并回退上一级")
            public Result falseTouchRejection(@PathVariable("id") Integer id) {
        try {
            interviewStatusService.falseTouchRejection(id);
            return Result.success().message("执行成功");
        } catch (Exception e) {
            return Result.error().message("执行失败");
        }

    }

    /**
     * 初始化面试状态接口
     *
     * @param id
     * @return
     */
    @PutMapping("/initialize/{id}")
    @Operation(summary = "初始化面试状态接口（有权限）", description = "将面试状态重置为最初状态")
    public Result initialize(@PathVariable("id") Integer id) {
        try {
            interviewStatusService.initialize(id);
            return Result.success().message("重置成功");
        } catch (Exception e) {
            return Result.error().message("重置失败");
        }

    }
}
