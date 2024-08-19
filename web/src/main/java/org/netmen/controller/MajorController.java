package org.netmen.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Pattern;
//import org.netmen.common.response.Result;
import org.netmen.common.result.Result;
import org.netmen.dao.po.Major;
import org.netmen.service.MajorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/major")
@Validated
@Tag(name = "major")
public class MajorController {
    @Autowired
    private MajorService majorService;

    //用来增加专业
    @PostMapping("/add")
    @Operation(summary = "增加专业",description = "要传入专业的名称和专业所属院的id")
    public Result add(String name, Integer collegeId){
        Major major = majorService.findMajorByName(name);
        //先检测一下是不是已经有这个专业了
        if(major==null){
            Major newOne = new Major();
            newOne.setName(name);
            newOne.setCollegeId(collegeId);
            boolean sus = majorService.save(newOne);
            if(sus){
                return Result.success();
            }else{
                return Result.error().message("添加失败！");
            }
        }
        return Result.error().message("此专业已存在");
    }

    //批量删除专业
    @DeleteMapping("/delete")
    @Operation(summary = "批量删除专业",description = "传入一个待删去专业的id的数组")
    public Result deleteMajor(@RequestParam List<Integer> ids){
        return majorService.deleteMajors(ids);
    }

    //修改专业信息
    @PutMapping("/updata")
    @Operation(summary = "修改专业名称",description = "传入待修改名称的专业的id、新名称、新的所属学院的id。如果名称不变，传入旧名称；如果学院不变，传入旧的学院id")
    public Result updata(@RequestParam Integer majorId,@RequestParam String name,@RequestParam Integer newCollegeId){
        return majorService.updataMajorName(majorId,name,newCollegeId);
    }

    //获取专业信息
    @GetMapping("/page")
    @Operation(summary = "获取专业列表",description = "传入学院id，获取对应的专业列表")
    public Result getMajotList(@RequestParam Integer collegeId,
                               @RequestParam(defaultValue = "1") Integer pageNum,
                               @RequestParam(defaultValue = "10") Integer pageSize){
        return majorService.getMajorList(collegeId,pageNum,pageSize);
    }

}
