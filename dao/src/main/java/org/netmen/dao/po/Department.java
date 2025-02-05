package org.netmen.dao.po;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import jakarta.validation.constraints.Pattern;
import lombok.Data;


@Data
@TableName("department")
public class Department extends BaseEntity  {

    @TableId(type = IdType.AUTO)
    private Integer id;

    @Pattern(regexp = "^\\S{1,255}$")
    private String name;

    private Integer organizationId;

}
