package org.netmen.dao.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
@TableName("interview_items")
public class InterviewItems extends BaseEntity{
    @TableId(type = IdType.AUTO)
    private Integer id;

    @Pattern(regexp = "^\\S{1,255}$")
    private String title;
    private Integer maxScore;
    private Integer minScore;
    private Integer failedScore;
    private Integer goodScore;

    @Pattern(regexp = "^\\S{1,255}$")
    private String remark;
}
