package org.netmen.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;
import org.netmen.dao.po.InterviewStatus;

@Mapper
public interface InterviewStatusMapper extends BaseMapper<InterviewStatus> {
    @Update("update test1.interview_status set deleted = 1 where student_id = #{id}")
    void updateDeleted(Integer id);
}
