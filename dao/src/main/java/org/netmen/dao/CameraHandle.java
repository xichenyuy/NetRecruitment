package org.netmen.dao;

import jakarta.servlet.ServletOutputStream;
import lombok.Data;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.FFmpegFrameRecorder;

import java.io.ByteArrayOutputStream;
import java.util.Map;

@Data
public class CameraHandle extends Thread {

    /**
     * 是否开始拉流解码
     */
    private boolean isStart = true;
    FFmpegFrameGrabber grabber;
    FFmpegFrameRecorder recorder;
    /**
     * 视频流
     */
    ByteArrayOutputStream pipedOutputStream;
    /**
     * 推流
     */
    Map<String, ServletOutputStream> streamMap = new HashMap<>();
    /**
     * 是否实时推流到客户端
     */
    private boolean isMonitoring = false;
    /**
     * flv header 输出流的头部信息
     */
    private byte[] flvHeader = null;
    /**
     * 没有客户链接的开始时间
     */
    protected long frameTime = System.currentTimeMillis();

    CameraImpl cameraImpl;
    Camera camera;
    String fileName;

    public CameraHandle() {
        pipedOutputStream = new ByteArrayOutputStream();
    }

    /**
     * 关闭流
     */
    public void close() {
        this.isStart = false;
    }

    /**
     * 拉取摄像头信息
     */
    @Override
    public void run() {
        try {
            fileName = camera.getCameraName() + DateTime.now().toString("yyyy_MM_dd") + ".flv";
            //设置日志级别
            avutil.av_log_set_level(avutil.AV_LOG_ERROR);
            //FFmpegLogCallback.set();
            init();
            int countErr = 0;
            while (isStart) {
                //获取画面  使用grabImage实时预览会等待2-3分钟
                Frame frame = grabber.grabFrame();
                if (frame != null) {
                    recorder.record(frame);
                    //业务需求 这个判断可以不要 设置太大实时预览会导致卡顿
                    if (pipedOutputStream.size() > camera.getFragmentSize()) {
                        //实时预览
                        sendFlvFrameData(pipedOutputStream.toByteArray());
                        //存储本地 用于历史回放
                        if (camera.getSavePlayback()) {
                            frameTime = System.currentTimeMillis();
                            if (Files.exists(Paths.get(fileName))) {
                                Files.write(Paths.get(fileName), pipedOutputStream.toByteArray(), StandardOpenOption.APPEND);
                            } else {
                                Files.write(Paths.get(fileName), flvHeader);
                                Files.write(Paths.get(fileName), pipedOutputStream.toByteArray(), StandardOpenOption.APPEND);
                            }
                        }
                        pipedOutputStream.reset();
                    }
                } else {
                    countErr++;
                    if (countErr > 10) {
                        System.out.println("拉流错误重启");
                        grabber.restart();
                        grabber.flush();
                        countErr = 0;
                    }
                }
                long date = System.currentTimeMillis();
                //5分钟没录制或预览关闭
                if ((date - frameTime) > camera.getRecoveryTime()) {
                    close();
                    System.out.println("退出："+camera.getCameraUrl());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                recorder.stop();
                recorder.release();
            } catch (FrameRecorder.Exception exception) {
                exception.printStackTrace();
            }
            try {
                grabber.stop();
            } catch (FrameGrabber.Exception exception) {
                exception.printStackTrace();
            }
            try {
                isMonitoring = false;
                camera.setMonitorState(0);
                camera.setSavePlayback(false);
                cameraImpl.updateById(camera);
                CameraImpl.cameraHandleMap.remove(camera.getId());
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    }

    private void init() throws FFmpegFrameRecorder.Exception, FrameGrabber.Exception {
        grabber = new FFmpegFrameGrabber(camera.getCameraUrl());
        // 超时时间(5秒)
        grabber.setOption("stimoout", "5000000");
        grabber.setOption("threads", "1");
        grabber.setPixelFormat(avutil.AV_PIX_FMT_YUV420P);
        // 设置缓存大小，提高画质、减少卡顿花屏
        grabber.setOption("buffer_size", "1024000");
        //grabber.setOption("buffer_size", "100");
        // 如果为rtsp流，增加配置
        if ("rtsp".equals(camera.getCameraUrl().substring(0, 4))) {
            // 设置打开协议tcp / udp
            grabber.setOption("rtsp_transport", "tcp");
            //首选TCP进行RTP传输
            grabber.setOption("rtsp_flags", "prefer_tcp");
            //设置超时时间
            // -stimeout的单位是us 微秒(1秒=1*1000*1000微秒)。
            grabber.setOption("stimeout", "5*1000*1000");
        }
        grabber.start();
        recorder = new FFmpegFrameRecorder(pipedOutputStream, grabber.getImageWidth(), grabber.getImageHeight(), grabber.getAudioChannels());
        // 设置比特率
        recorder.setVideoBitrate(grabber.getVideoBitrate());
        recorder.setFormat("flv");
           /* recorder.setInterleaved(false);
            recorder.setVideoOption("tune", "zerolatency");
            recorder.setVideoOption("preset", "ultrafast");
            recorder.setVideoOption("crf", "26");
            recorder.setVideoOption("threads", "1");
            recorder.setVideoCodecName("libx264");*/
        recorder.setVideoCodec(avcodec.AV_CODEC_ID_H264);
        recorder.setSampleRate(grabber.getSampleRate());
        recorder.setAudioChannels(grabber.getAudioChannels());
        recorder.setPixelFormat(recorder.getPixelFormat());
        recorder.setAudioCodec(avcodec.AV_CODEC_ID_AAC);
        // 视频帧率(保证视频质量的情况下最低25，低于25会出现闪屏)
        recorder.setFrameRate(grabber.getFrameRate());
        // 关键帧间隔，一般与帧率相同或者是视频帧率的两倍
        recorder.setGopSize((int) grabber.getFrameRate() * 2);
        recorder.setVideoQuality(1.0);

        //获取头文件信息
        recorder.start();
        grabber.flush();
        flvHeader = pipedOutputStream.toByteArray();
        pipedOutputStream.reset();
    }

    private void sendFlvFrameData(byte[] bytes) {
        try {
            if (streamMap.size()>0&& isMonitoring) {
                frameTime = System.currentTimeMillis();
                Iterator<Map.Entry<String, ServletOutputStream>> iterator = streamMap.entrySet().iterator();
                while (iterator.hasNext()) {
                    Map.Entry<String, ServletOutputStream> next = iterator.next();
                    ServletOutputStream outputStream = null;
                    try {
                        outputStream = next.getValue();
                        if (bytes.length > 0) {
                            outputStream.write(bytes, 0, bytes.length);
                            outputStream.flush();
                        }
                    } catch (Exception e) {
                        try {
                            if (null != outputStream) {
                                outputStream.close();
                            }
                        } catch (IOException ioException) {
                            ioException.printStackTrace();
                        }
                        iterator.remove();
                        camera.setViewsNumber(camera.getViewsNumber()-1);
                        cameraImpl.updateById(camera);
                        System.out.println("响应输出流出现错误");
                        e.printStackTrace();
                    }
                }
            }
        }catch (Exception e) {

        }
    }

}
 