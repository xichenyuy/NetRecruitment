package org.netmen.service;

import jakarta.servlet.http.HttpServletResponse;

public interface ICameraService {
    /**
     * 获取是否存在推送流，并返回一个唯一key 用于获取flv流
     */
    public String getCamera(Long id);

    /**
     * 开启录制
     */
    public boolean openRecord(Long id);

    /**
     * 关闭录制
     */
    public boolean closeRecord(Long id);

    /**
     * 客户端关闭推送flv流
     */
    public boolean closeHttpFlv(Long id,String cameraKey);

    /**
     * 推送flv流
     */
    public void getHttpFlv(Long id,String cameraKey, HttpServletResponse response);
}
