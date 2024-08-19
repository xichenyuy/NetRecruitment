package org.netmen.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoleDTO {
    private String name;
    private String roleKey;
    private Boolean disabled;
    private String remark;
}
