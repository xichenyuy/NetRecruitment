package org.netmen.dao.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Permission {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String name;
    private String perms;
    private String path;
    private String component;
    private String icon;
    private Boolean hidden;
    private Boolean disabled;
    private Boolean deleted;
    private Integer createBy;
    private LocalDateTime createTime;
    private Integer updateBy;
    private LocalDateTime updateTime;
    private String remark;

}
