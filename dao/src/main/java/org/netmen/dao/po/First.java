package org.netmen.dao.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("first")
public class First {
    //驼峰命名法
    @TableId(value = "first_id", type = IdType.INPUT)
    private Integer firstId;
    private Integer departmentId;
}
