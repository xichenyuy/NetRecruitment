package org.netmen.dao.po;

import com.baomidou.mybatisplus.annotation.TableName;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
@TableName("interview_record")
public class InterviewRecord extends BaseEntity{
    @NotEmpty
    private Integer studentId;
    @NotEmpty
    private Integer departmentId;
    private Boolean failed;
    private Integer priority;
}
