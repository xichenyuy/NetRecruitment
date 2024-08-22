package org.netmen.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
//import org.netmen.common.response.Result;
import org.netmen.common.result.Result;
import org.netmen.dao.mapper.OrganizationMapper;
import org.netmen.dao.po.Organization;
import org.netmen.service.OrganizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class OrganizationServeImpl extends ServiceImpl<OrganizationMapper, Organization> implements OrganizationService{

    @Autowired
    private OrganizationMapper organizationMapper;


    @Override
    public Organization findByOrganizationName(String name) {
        /*QueryWrapper：由于是字符串形式的条件拼接，编译时无法检查其中的错误和类型不匹配问题，所以需要开发人员自行保证查询条件的正确性。
        LambdaQueryWrapper：基于 Lambda 表达式构建查询条件，可以在编译阶段进行类型检查，编译器可以帮助检测属性名等错误，减少运行时出错的可能性。*/
        LambdaQueryWrapper<Organization> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(Organization::getName,name);
        return organizationMapper.selectOne(wrapper);
    }

    @Override
    public Result deleteOrganization(List<Integer> ids) {
        if (ids==null||ids.size()==0){
            return Result.error().message("未传入待删除的组织");
        }
        //建立起一个organization在数据库中的查询器
        LambdaQueryWrapper<Organization> wrapper = new LambdaQueryWrapper<>();
        //利用查询器，在数据库中查出所有的对应id的组织
        wrapper.in(Organization::getOrganizationId,ids);
        //查看删除状态
        int i = organizationMapper.delete(wrapper);
        if(i==0){
            return Result.error().message("删除失败");
        }
        return Result.success().message("删除成功！");
    }

    @Override
    @Transactional(rollbackFor = Exception.class) // 确保事务性
    public Result changeOrganizationName(Integer organizationId, String newName) {
        if (newName == null || newName.trim().isEmpty()) {
            return Result.error().message("组织名称不能为空");
        }

        //建立查询器
        LambdaQueryWrapper<Organization> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Organization::getOrganizationId,organizationId);
        //用mapper映射操作数据库，修改对应字段

        //用mapper查询数据库
        Organization organization = organizationMapper.selectOne(wrapper);

        if(organization!=null){
            organization.setName(newName);
            //修改对应组织名称
            int updateResult = organizationMapper.updateById(organization);
            //更新数据库
            if(updateResult==1){
                return Result.success();
            }else{
                return Result.error().message("修改失败");
            }
        }
        return Result.error().message("没有找到该组织");
    }

    @Override
    public Result getOrganizationList(Integer currentPage, Integer size) {
        //分页器
        Page<Organization> page = new Page<>(currentPage,size);
        //根据分页器拿到数据
        IPage<Organization> organizationIPage = organizationMapper.selectAllOrganization(page);
        if(organizationIPage==null){
            return Result.error().message("数据库中暂无数据");
        }
        //处理从数据库查询得到的数据，并将其转换成一个列表形式的Java集合
        List<Map<String,Object>> collect = (List<Map<String, Object>>) organizationIPage.getRecords().stream().map(organization -> {
            try {
                return convertOrganizationToMap(organization);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }).toList();
        return Result.success().data(collect);
    }
    //用来把从数据库拿出来的数据转换成map的函数
    private Map<String,Object> convertOrganizationToMap(Organization organization) throws ParseException {
        HashMap<String,Object> map = new HashMap<>();
        map.put("organizationId",organization.getOrganizationId());
        map.put("name",organization.getName());
        return map;
    }
}
