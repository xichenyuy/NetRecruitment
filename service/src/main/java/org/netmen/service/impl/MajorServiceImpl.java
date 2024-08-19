package org.netmen.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.netmen.common.result.Result;
import org.netmen.dao.mapper.MajorMapper;
import org.netmen.dao.po.Major;
import org.netmen.service.MajorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MajorServiceImpl extends ServiceImpl<MajorMapper, Major> implements MajorService {
    @Autowired
    private MajorMapper majorMapper;

    @Override
    public Major findMajorByName(String name) {
        LambdaQueryWrapper<Major> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Major::getName,name);
        return majorMapper.selectOne(wrapper);
    }

    @Override
    public Result deleteMajors(List<Integer> ids) {
        if(ids == null||ids.size() == 0){
            return Result.error().message("传入的ids为空");
        }
        LambdaQueryWrapper<Major> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(Major::getMajorId,ids);

        int res = majorMapper.delete(wrapper);
        if(res!=0){
            return Result.success();
        }
        //但是这里有问题噢。。。。怎么样才能检测ids都被删了。。。！
        return Result.error().message("删除失败！");
    }

    @Override
    public Result updataMajorName(Integer majorId, String name, Integer newCollegeId) {
        if(name == null || name.trim().isEmpty()){
            return Result.error().message("专业名称不能为空！");
        }
        if(newCollegeId == 0){
            return Result.error().message("专业所属院系不能为空");
        }
        LambdaQueryWrapper<Major> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Major::getMajorId,majorId);
        Major major = majorMapper.selectOne(wrapper);
        if(major != null){
            if(name != major.getName()){
                major.setName(name);
            }
            if(newCollegeId != major.getCollegeId()){
                major.setCollegeId(newCollegeId);
            }
            int res = majorMapper.updateById(major);
            if(res == 1){
                return Result.success();
            }

            return Result.error().message("修改名称失败！");
        }
        return Result.error().message("此专业不存在！");
    }

    @Override
    public Result getMajorList(Integer collegeId, Integer pageNum, Integer pageSize) {
        Page<Major> page = new Page<>(pageNum,pageSize);
        IPage<Major> ipage = majorMapper.selectMapsPage(page,collegeId);

        if(ipage == null){
            return Result.error().message("数据库中暂无数据");
        }

        List<Map<String,Object>> collection = ipage.getRecords().stream().map(major -> {
            try {
                return convertMajorToMap(major);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }).toList();
        return Result.success().data(collection);
    }

    private Map<String,Object> convertMajorToMap(Major major) throws ParseException {
        HashMap<String,Object> map = new HashMap<>();
        map.put("college_id",major.getCollegeId());
        map.put("name",major.getName());
        map.put("major_id",major.getMajorId());
        return map;
    }

}
