package org.netmen.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.netmen.common.response.Result;
import org.netmen.dao.po.College;
import org.netmen.service.CollegeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/college")
@Validated
@Tag(name = "college")
public class CollegeController {

    @Autowired
    private CollegeService collegeService;

    //增加组织
    @PostMapping("/add")
    @Operation(summary = "增加学院",description = "传入学院名称即可")
    public Result add(String name){
        College college = collegeService.findByCollegeName(name);
        //先检测一下数据库里是不是已经存在这个组织了
        if(college == null){
            System.out.println("新增学院名:"+name);
            College newOne = new College();
            newOne.setName(name);
            collegeService.save(newOne);
            return Result.success();
        }
        return Result.error("该学院已存在");
    }

    //批量删除组织
    @GetMapping("/delete")
    @Operation(summary = "删除学院",description = "将被选中的多个学院的id放入一个数字数组中，再传过来")
    public Result deleteCollege(@RequestParam List<Integer> ids){
        return collegeService.deleteCollege(ids);
    }

    //获取组织列表
    @GetMapping("/page")
    @Operation(summary = "获取学院列表",description = "直接调用这个接口，在data里找就好")
    public Result getCollegeList(@RequestParam(defaultValue = "1")Integer currentPage,
                                 @RequestParam(defaultValue = "10")Integer size){
        //使用的是分页查询法
        return collegeService.getCollegeList(currentPage,size);
    }

    //修改单个组织
    @PostMapping("/change")
    @Operation(summary = "修改单个学院",description = "传入待修改学院的id和新名称")
    public Result changeCollegeName(@RequestParam Integer collegeId,@RequestParam String newName){
        return collegeService.changeCollegeName(collegeId,newName);
    }

}