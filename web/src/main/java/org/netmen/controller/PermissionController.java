package org.netmen.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.netmen.common.result.Result;
import org.netmen.common.utils.BeanCopyUtil;
import org.netmen.dao.po.Permission;
import org.netmen.dto.PermissionDTO;
import org.netmen.service.PermissionService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/permission")
@Tag(name = "permission")
public class PermissionController {
    @Autowired
    private PermissionService permissionService;

    @GetMapping
    public Result list(Integer pageNum, Integer pageSize){
        //返回
        //List<Permission> list = permissionService.list();
        //return Result.success().data(list);
        //分页返回
        Page<Permission> page = new Page<>(pageNum, pageSize);  //Page为IPage接口的实现类 MP实现了简单分页模型
        Page<Permission> permissionPage = permissionService.page(page);
        return Result.success().data(permissionPage);
        //List拷贝类的使用样例 暂时不需要封装PageVo
        //List<UserVo> userVos = BeanCopyUtils.copyBeanList(userPage.getRecords(), UserVo.class);
        //PageVo pageVo = new PageVo(userVos, userPage.getTotal());
        //TODO
        //增加筛选条件
    }

    @PostMapping
    public Result add(@RequestBody PermissionDTO permissionDTO){
        Permission permission = new Permission();
        BeanUtils.copyProperties(permissionDTO, permission);
        //Permission permission1 = BeanCopyUtil.copyBean(permissionDTO, Permission.class);  //使用自定义封装的拷贝工具
        permissionService.save(permission);
        return Result.success();
        //TODO
        //校验name perms唯一性 或者在数据库中设置唯一索引
        //插入后返回插入后数据库中的数据
    }

    @PutMapping("/{id}")
    public Result update(@PathVariable Integer id, @RequestBody PermissionDTO permissionDTO){
        Permission byId = permissionService.getById(id);
        if(byId == null){
            return Result.error().message("ID不存在");
        }
        Permission permission = new Permission();
        BeanUtils.copyProperties(permissionDTO, permission);
        permission.setId(id);
        permissionService.updateById(permission);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id){
        Permission byId = permissionService.getById(id);
        if(byId == null){
            return Result.error().message("ID不存在");
        }
        permissionService.removeById(id);
        return Result.success();
    }
}
