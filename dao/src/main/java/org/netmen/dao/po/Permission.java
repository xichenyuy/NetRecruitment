package org.netmen.dao.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;


@EqualsAndHashCode(callSuper = true)    //用于确保equals和hashCode包含父类一起比较
@Data
public class Permission extends BaseEntity {
    private String name;
    private String perms;
    private String path;
    private String component;
    private String icon;
    private Boolean hidden;
    private Boolean disabled;
    private String remark;
}
