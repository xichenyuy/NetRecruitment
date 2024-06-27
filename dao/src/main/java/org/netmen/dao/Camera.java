package org.netmen.dao;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Camera  {
    /**
     * 摄像头
     */
    private String cameraName;
    /**
     *摄像头地址
     */
    private String cameraUrl;
    /**
     *观看人数
     */
    private Integer viewsNumber;
    /**
     *摄像头状态
     */
    private Integer monitorState;
    /**
     *闲置关闭时间
     */
    private Integer recoveryTime;
    /**
     *保存回放
     */
    private Boolean savePlayback;
    /**
     *分片大小
     */
    private Integer fragmentSize;
}