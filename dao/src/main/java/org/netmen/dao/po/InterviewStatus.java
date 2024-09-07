package org.netmen.dao.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@TableName("interview_status")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InterviewStatus extends BaseEntity {
    @TableId(value = "student_id")
    private Integer id;
    private Integer firstDepartmentId;
    private Integer secondDepartmentId;
    // 调剂(0不接受 1接受)
    private Boolean adjust;
    // 当前状态(0面试中 1通过 2淘汰)
    private Short status;
    private Integer curDepartmentId;

}
