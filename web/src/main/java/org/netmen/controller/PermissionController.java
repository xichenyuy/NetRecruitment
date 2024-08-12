package org.netmen.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.netmen.common.result.Result;
import org.netmen.dao.po.Permission;
import org.netmen.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/permission")
@Tag(name = "permission")
public class PermissionController {
    @Autowired
    private PermissionService permissionService;

    @GetMapping
    private Result list(){
        return Result.success();
    }
    //TODO
    //分页



    @PostMapping
    private Result add(@RequestBody Permission permission){
        return Result.success();
    }

    //检查Permission对象在swagger中有无参数提示
}
