package org.netmen.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PermissionDTO {
    private String name;
    private String perms;
    private String path;
    private String component;
    private String icon;
    private Boolean hidden;
    private Boolean disabled;
    private String remark;
}
