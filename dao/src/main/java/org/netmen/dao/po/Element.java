package org.netmen.dao.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("element")
public class Element {
    @TableId(type = IdType.AUTO)
    private Integer elementId;
    private String factor;
    private String content;
}