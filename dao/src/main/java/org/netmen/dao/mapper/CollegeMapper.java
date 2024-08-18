package org.netmen.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.netmen.dao.po.College;

import java.util.List;

@Mapper
public interface CollegeMapper extends BaseMapper<College> {
    IPage<College> selectAllCollege(Page<College> page);

}
