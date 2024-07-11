package org.netmen.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.websocket.server.PathParam;
import lombok.extern.slf4j.Slf4j;
import org.bytedeco.javacpp.Loader;
import org.netmen.common.utils.MediaVideoTransfer;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

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





}


