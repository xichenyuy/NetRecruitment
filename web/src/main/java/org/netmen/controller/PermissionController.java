package org.netmen.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.netmen.common.result.Result;
import org.netmen.dao.po.Permission;
import org.netmen.dto.PermissionDTO;
import org.netmen.service.PermissionService;
import org.springframework.beans.BeanUtils;
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
    //git修改测试
    //test2




    @PostMapping
    private Result add(@RequestBody PermissionDTO permissionDTO){
        Permission permission = new Permission();
        BeanUtils.copyProperties(permissionDTO, permission);
        permissionService.save(permission);
        return Result.success();
    }

    //检查Permission对象在swagger中有无参数提示 对象也会有参数提示
}
