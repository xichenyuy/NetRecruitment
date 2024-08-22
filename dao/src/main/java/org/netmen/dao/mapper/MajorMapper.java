package org.netmen.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.netmen.dao.po.Major;

@Mapper
public interface MajorMapper extends BaseMapper<Major> {
    IPage<Major> selectMapsPage(Page<Major> page, @Param("collegeId")Integer collegeId);
}
