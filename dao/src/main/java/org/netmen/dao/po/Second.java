package org.netmen.dao.po;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("second")
public class Second {
    //驼峰命名法
    @TableId(value = "second_id", type = IdType.INPUT)
    private Integer secondId;
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private Integer departmentId;
}
