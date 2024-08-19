package org.netmen.dao.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("interview")
public class Interview {
    @TableId(type = IdType.AUTO)
    private Integer interviewId;
    private String name;
 }