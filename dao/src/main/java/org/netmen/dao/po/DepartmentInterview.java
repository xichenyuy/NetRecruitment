package org.netmen.dao.po;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;


@Data
@TableName("department_interview")
public class DepartmentInterview extends BaseEntity {

    @TableId(type = IdType.AUTO)
    private Integer id;

    private Integer departmentId;
    private Integer interviewId;
    private Integer priority;

}
