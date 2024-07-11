package org.netmen.dao.dto;

import lombok.Data;

@Data
public class RtspDto {
    private String rtspUrl;

    public void setRtspUrl(String rtspUrl) {
        this.rtspUrl = rtspUrl;
    }
}