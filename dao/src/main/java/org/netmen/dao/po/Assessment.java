package org.netmen.dao.po;

import com.baomidou.mybatisplus.annotation.TableName;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
@TableName("assessment")
public class Assessment extends BaseEntity{
    @NotEmpty
    private Integer studentId;
    @NotEmpty
    private Integer interviewInterviewItemsId;//表中的interview_interview_items_id
    private Integer score;
    private String account;
}
