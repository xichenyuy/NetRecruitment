package org.netmen.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.netmen.common.result.Result;
import org.netmen.dao.po.Element;
import org.netmen.service.ElementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("element")
@Validated
@Tag(name = "element")
public class ElementController {
    @Autowired
    private ElementService elementService;

    @PostMapping()
    @Operation(summary = "面试项的名字", description = "面试项的名字")
    public Result addfactor(String factor){
        Element element = new Element();
        element.setFactor(factor);
        elementService.save(element);
        return Result.success();
    }

    @PostMapping()
    @Operation(summary = "面试项的具体内容", description = "面试项的具体内容")
    public Result addcontent(String content){
        Element element = new Element();
        element.setContent(content);
        elementService.save(element);
        return Result.success();
    }
}
