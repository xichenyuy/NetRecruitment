package org.netmen.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.netmen.common.result.Result;
import org.netmen.dao.po.Role;
import org.netmen.dto.RoleDTO;
import org.netmen.service.RoleService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/role")
@Tag(name = "role")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @GetMapping
    @PreAuthorize("hasAnyAuthority('role:list')")
    public Result<Object> list(Integer pageNum, Integer pageSize) {
        Page<Role> page = new Page<>(pageNum, pageSize);
        Page<Role> rolePage = roleService.page(page);
        return Result.success().data(rolePage);
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('role:add')")
    public Result<Object> add(@RequestBody RoleDTO roleDTO) {
        Role role = new Role();
        BeanUtils.copyProperties(roleDTO, role);
        roleService.save(role);
        return Result.success();
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('role:update')")
    public Result<Object> update(Integer id, @RequestBody RoleDTO roleDTO) {
        Role role = roleService.getById(id);
        if(role == null){
            return Result.error().message("ID不存在");
        }
        BeanUtils.copyProperties(roleDTO, role);
        roleService.updateById(role);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('role:delete')")
    public Result<Object> delete(Integer id) {
        Role byId = roleService.getById(id);
        if(byId == null){
            return Result.error().message("ID不存在");
        }
        roleService.removeById(id);
        return Result.success();
    }
}
