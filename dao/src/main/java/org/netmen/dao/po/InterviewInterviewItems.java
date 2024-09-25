package org.netmen.dao.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
@TableName("interview_interview_items")
public class InterviewInterviewItems extends BaseEntity{
    @NotEmpty
    private int interviewId;
    @NotEmpty
    private int interviewItemsId;
}
