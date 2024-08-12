package org.netmen.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/role")
@Tag(name = "role")
public class RoleController {

    @GetMapping
    @PreAuthorize("hasAnyAuthority('role:list')")
    public String list() {
        return "role list";
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('role:add')")
    public String add() {
        return "新增成功";
    }

    @PutMapping
    @PreAuthorize("hasAnyAuthority('role:update')")
    public String update() {
        return "更新成功";
    }

    @DeleteMapping
    @PreAuthorize("hasAnyAuthority('role:delete')")
    public String delete() {
        return "删除成功";
    }
}
