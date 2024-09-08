package org.netmen.dao.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.netmen.dao.po.InterviewInterviewItems;
@Mapper
public interface InterviewInterviewItemsMapper extends BaseMapper<InterviewInterviewItems> {
    IPage<InterviewInterviewItems> selectMaps(Page<InterviewInterviewItems> page, LambdaQueryWrapper<InterviewInterviewItems> wrapper);
}
