package org.netmen.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.netmen.common.result.Result;
import org.netmen.dao.po.Interview;
import org.netmen.service.InterviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("interview")
@Validated
@Tag(name = "interview")
public class InterviewController {

    @Autowired
    private InterviewService interviewService;

    @PostMapping()
    @Operation(summary = "面试者信息", description = "面试者信息")
    public Result add(String name){
        Interview interview = new Interview();
        interview.setName(name);
        interviewService.save(interview);
        return Result.success();
    }
}
