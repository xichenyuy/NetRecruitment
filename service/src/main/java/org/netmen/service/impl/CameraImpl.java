package org.netmen.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.netmen.dao.Camera;
import org.netmen.dao.mapper.CameraMapper;
import org.netmen.service.ICameraService;

public class CameraImpl extends ServiceImpl<CameraMapper, Camera> implements ICameraService {
    public static Map<Long,CameraHandle> cameraHandleMap = new HashMap<>();
    public static List<String> cameraKeyList = new ArrayList<>();
    /**
     * 获取是否存在推送流，并返回一个唯一key 用于获取flv流
     */
    @Override
    public String getCamera(Long id) {
        if (!cameraHandleMap.containsKey(id)){
            Camera camera = getById(id);
            camera.setMonitorState(1);
            CameraHandle cameraHandle = new CameraHandle();
            cameraHandle.setCamera(camera);
            cameraHandle.setCameraImpl(this);
            cameraHandle.start();
            cameraHandleMap.put(id,cameraHandle);
            updateById(camera);
        }
        String uuid = UUID.randomUUID().toString().replace("-","");
        cameraKeyList.add(uuid);
        return uuid;
    }
    /**
     * 开启录制
     */
    @Override
    public boolean openRecord(Long id) {
        Camera camera = getById(id);
        camera.setMonitorState(1);
        camera.setSavePlayback(true);
        if (!cameraHandleMap.containsKey(id)){
            CameraHandle cameraHandle = new CameraHandle();
            cameraHandle.setCamera(camera);
            cameraHandle.setCameraImpl(this);
            cameraHandle.start();
            cameraHandleMap.put(id,cameraHandle);
        }else{
            cameraHandleMap.get(id).setCamera(camera);
        }
        updateById(camera);
        return true;
    }
    /**
     * 关闭录制
     */
    @Override
    public boolean closeRecord(Long id) {
        if (cameraHandleMap.containsKey(id)){
            Camera camera = getById(id);
            camera.setSavePlayback(false);
            updateById(camera);
            cameraHandleMap.get(id).setCamera(camera);
        }
        return true;
    }
    /**
     * 客户端关闭推送flv流
     */
    @Override
    public boolean closeHttpFlv(Long id,String cameraKey) {
        if (cameraHandleMap.containsKey(id)){
            CameraHandle cameraHandle = cameraHandleMap.get(id);
            cameraHandle.streamMap.remove(cameraKey);
            Camera camera = new Camera();
            camera.setId(id);
            camera.setViewsNumber(camera.getViewsNumber()-1);
            updateById(camera);
        }
        return true;
    }
    /**
     * 推送flv流
     */
    @Override
    public void getHttpFlv(Long id,String cameraKey, HttpServletResponse response) {
        try{
            if (cameraHandleMap.containsKey(id)&&cameraKeyList.contains(cameraKey)) {
                cameraKeyList.remove(cameraKey);
                CameraHandle cameraHandle = cameraHandleMap.get(id);
                response.setStatus(HttpStatus.OK.value());
                response.addHeader("Content-Disposition", "attachment;filename=\"" + java.net.URLEncoder.encode(cameraHandle.fileName)+ "\"");
                response.setContentType("video/x-flv");
                response.setHeader("Connection", "keep-alive");
                response.setHeader("accept_ranges", "bytes");
                response.setHeader("pragma", "no-cache");
                response.setHeader("cache_control", "no-cache");
                response.setHeader("transfer_encoding", "CHUNKED");
                response.setHeader("SERVER", "hmsm");
                ServletOutputStream stream = response.getOutputStream();
                stream.write(cameraHandle.getFlvHeader());
                stream.flush();
                cameraHandle.streamMap.put(cameraKey,stream);
                cameraHandle.setMonitoring(true);
                try {
                    Camera camera = cameraHandle.getCamera();
                    camera.setViewsNumber(camera.getViewsNumber()+1);
                    updateById(camera);
                }catch (Exception e){
                    e.printStackTrace();
                }
                while (cameraHandle.streamMap.containsKey(cameraKey)){

                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
