package org.netmen.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.netmen.dao.po.Organization;

@Mapper
public interface OrganizationMapper extends BaseMapper<Organization> {

    IPage<Organization> selectAllOrganization(Page<Organization> page);
}
