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


    @PostMapping("/rtspToFlvJava")
    @Operation(summary = "获取视频", description = "视频获取")
    public ApiResponse rtspToFlvJava(@RequestBody RtspDto rtspDto) {
        String rtspUrl = rtspDto.getRtspUrl();
        MediaVideoTransfer mediaVideoTransfer = new MediaVideoTransfer();
        mediaVideoTransfer.setSrc(rtspUrl);
        mediaVideoTransfer.setTarget("rtmp://localhost:1935/myapp/yang");
        mediaVideoTransfer.setTransportType("tcp");

        Future<?> future = executorService.submit(()->{
                mediaVideoTransfer.live();
        });
        return ApiResponse.success("http://localhost:80/live?port=1935&app=myapp&stream=yang");
    }
}


