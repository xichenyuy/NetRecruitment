package org.netmen.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.ibatis.annotations.Param;
import org.netmen.common.result.Result;
import org.netmen.dao.mapper.CollegeMapper;
import org.netmen.dao.mapper.UserMapper;
import org.netmen.dao.po.College;
import org.netmen.dao.po.User;
import org.netmen.service.CollegeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CollegeServeImpl extends ServiceImpl<CollegeMapper, College> implements CollegeService{

    @Autowired
    private CollegeMapper collegeMapper;


    @Override //指明重写超类的方法
    public College findByCollegeName(String name) {
       /*QueryWrapper：由于是字符串形式的条件拼接，编译时无法检查其中的错误和类型不匹配问题，所以需要开发人员自行保证查询条件的正确性。
        LambdaQueryWrapper：主要用于构造SQL查询中的WHERE条件部分，基于 Lambda 表达式构建查询条件，可以在编译阶段进行类型检查，编译器可以帮助检测属性名等错误，减少运行时出错的可能性。*/
        LambdaQueryWrapper<College> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(College::getName,name);
        return collegeMapper.selectOne(wrapper);
    }

    @Override
    public Result deleteCollege(List<Integer> ids) {
        if (ids==null||ids.size()==0){
            return Result.error().message("未传入待删除的学院");
        }
        //建立起一个organization在数据库中的查询器
        LambdaQueryWrapper<College> wrapper = new LambdaQueryWrapper<>();
        //利用查询器，在数据库中查出所有的对应id的组织
        wrapper.in(College::getCollegeId,ids);
        //查看删除状态
        int i = collegeMapper.delete(wrapper);
        if(i==0){
            return Result.error().message("删除失败");
        }
        return Result.success().message("删除成功！");
    }

    @Override
    @Transactional(rollbackFor = Exception.class) // 确保事务性
    public Result changeCollegeName(Integer collegeId, String newName) {
        if (newName == null || newName.trim().isEmpty()) {
            return Result.error().message("学院名称不能为空");
        }

        //建立查询器
        LambdaQueryWrapper<College> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(College::getCollegeId,collegeId);
        //用mapper映射操作数据库，修改对应字段

        //用mapper查询数据库
        College college = collegeMapper.selectOne(wrapper);

        if(college!=null){
            college.setName(newName);
            //修改对应组织名称
            int updateResult = collegeMapper.updateById(college);
            //更新数据库
            if(updateResult==1){
                return Result.success();
            }else{
                return Result.error().message("修改失败");
            }
        }
        return Result.error().message("没有找到该学院");
    }

    @Override
    public Result getCollegeList(Integer currentPage, Integer size) {
        //分页器
        Page<College> page = new Page<>(currentPage,size);
        //根据分页器拿到数据
        IPage<College> collegeIPage = collegeMapper.selectAllCollege(page);
        if(collegeIPage==null){
            return Result.error().message("数据库中暂无数据");
        }
        //处理从数据库查询得到的数据，并将其转换成一个列表形式的Java集合
        List<Map<String,Object>> collect = (List<Map<String, Object>>) collegeIPage.getRecords().stream().map(college -> {
            try {
                return convertcollegeToMap(college);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }).toList();
        return Result.success().data(collect);
    }
    //用来把从数据库拿出来的数据转换成map的函数
    private Map<String,Object> convertcollegeToMap(College college) throws ParseException {
        HashMap<String,Object> map = new HashMap<>();
        map.put("collegeId",college.getCollegeId());
        map.put("name",college.getName());
        return map;
    }
}