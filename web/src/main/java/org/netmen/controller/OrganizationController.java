package org.netmen.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
//import org.netmen.common.response.Result;
import org.netmen.common.result.Result;
import org.netmen.dao.po.Organization;
import org.netmen.service.OrganizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/organization")
@Validated
@Tag(name = "organization")
public class OrganizationController {

    @Autowired
    private OrganizationService organizationService;

    //增加组织
    @PostMapping("/add")
    @Operation(summary = "增加组织",description = "传入组织名称即可")
    public Result add(String name){
        Organization organization = organizationService.findByOrganizationName(name);
        //先检测一下数据库里是不是已经存在这个组织了
        if(organization == null){
            System.out.println("新增组织名:"+name);
            Organization newOne = new Organization();
            newOne.setName(name);
            organizationService.save(newOne);
            return Result.success().message("新建组织成功");
        }
        return Result.error().message("该组织已存在");
    }

    //批量删除组织
    @DeleteMapping("/delete")
    @Operation(summary = "删除组织",description = "将被选中的多个组织的id放入一个数字数组中，再传过来")
    public Result deleteOrganization(@RequestParam List<Integer> ids){
        return organizationService.deleteOrganization(ids);
    }

    //获取组织列表
    @GetMapping("/page")
    @Operation(summary = "获取组织列表",description = "直接调用这个接口，在data里找就好")
    public Result getOrganizationList(@RequestParam(defaultValue = "1")Integer currentPage,
                                      @RequestParam(defaultValue = "10")Integer size){
        //使用的是分页查询法
        return organizationService.getOrganizationList(currentPage,size);
    }

    //修改单个组织
    @PutMapping("/change")
    @Operation(summary = "修改单个组织",description = "传入待修改名字组织的id和新名称")
    public Result changeOrganizationName(@RequestParam Integer organizationId,@RequestParam String newName){
        return organizationService.changeOrganizationName(organizationId,newName);
    }

}
