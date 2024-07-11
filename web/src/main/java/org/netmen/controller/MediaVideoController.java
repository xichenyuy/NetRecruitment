package org.netmen.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.websocket.server.PathParam;
import lombok.extern.slf4j.Slf4j;
import org.bytedeco.javacpp.Loader;
import org.netmen.common.utils.MediaVideoTransfer;
import org.netmen.dao.dto.ApiResponse;
import org.netmen.dao.dto.RtspDto;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Slf4j
@RestController
@Tag(name = "MediaVideo")
public class MediaVideoController {
    @GetMapping("/video")
    @Operation(summary = "获取视频", description = "视频获取")
    public void list(){
        MediaVideoTransfer mediaVideoTransfer = new MediaVideoTransfer();
        mediaVideoTransfer.setSrc("rtsp://114.132.181.5:8554/camera_test");
        mediaVideoTransfer.setTarget("rtmp://localhost:1935/myapp/yang");
        mediaVideoTransfer.setTransportType("tcp");
        mediaVideoTransfer.live();
    }


    @GetMapping("/video2")
    @Operation(summary = "获取视频", description = "视频获取")
    public void list2(){
        MediaVideoTransfer mediaVideoTransfer = new MediaVideoTransfer();
        mediaVideoTransfer.setSrc("rtsp://localhost:554/live");
        mediaVideoTransfer.setTarget("rtmp://127.0.0.1:1935/myapp/yang");
        mediaVideoTransfer.setTransportType("upd");
        mediaVideoTransfer.live();
    }


    @GetMapping("/video3")
    @Operation(summary = "获取视频", description = "视频获取")
    public void list3() {
        // rtsp地址
        String rtspDir = "rtsp://114.132.181.5:8554/camera_test";
        // rtmp地址
        String rtmpDir = "rtmp://localhost:1935/myapp/yyy";

        String ffmpeg = Loader.load(org.bytedeco.ffmpeg.ffmpeg.class);

        ProcessBuilder pb = new ProcessBuilder(ffmpeg,
                "-re",
                "-rtsp_transport", "tcp",
                "-i", rtspDir,
                "-f", "flv",
                "-vcodec", "copy",
//                "-vcodec", "h264",
//                "-vprofile", "baseline",
//                "-acodec", "aac",
                "-acodec", "copy",
                "-ar", "44100",
                "-ac", "1",
                "-q:v", "10",
                rtmpDir
        );
        try{
            pb.inheritIO().start().waitFor();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }


    ExecutorService executorService = Executors.newFixedThreadPool(10); //创建线程池

    @PostMapping("/rtspToFlv")
    @Operation(summary = "rtsp流转flv", description = "rtsp流转flv和rtmp")
    public ApiResponse rtspToFlv(@RequestBody RtspDto rtspDto) {
        // rtsp地址
        String rtspUrl = rtspDto.getRtspUrl();
        // rtmp地址
        String rtmpDir = "rtmp://localhost:1935/myapp/yang";

        String ffmpeg = Loader.load(org.bytedeco.ffmpeg.ffmpeg.class);

        ProcessBuilder pb = new ProcessBuilder(ffmpeg,
                "-re",
                "-rtsp_transport", "tcp",
                "-i", rtspUrl,
                "-f", "flv",
                "-vcodec", "copy",
//                "-vcodec", "h264",
//                "-vprofile", "baseline",
//                "-acodec", "aac",
                "-acodec", "copy",
                "-ar", "44100",
                "-ac", "1",
                "-q:v", "10",
                rtmpDir
        );
        Future<?> future = executorService.submit(()->{
            try{
                pb.inheritIO().start().waitFor();
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        return ApiResponse.success("http://localhost:80/live?port=1935&app=myapp&stream=yang");
    }
}


