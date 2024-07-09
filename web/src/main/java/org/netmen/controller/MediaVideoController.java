package org.netmen.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.websocket.server.PathParam;
import lombok.extern.slf4j.Slf4j;
import org.netmen.common.utils.MediaVideoTransfer;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@Tag(name = "MediaVideo")
public class MediaVideoController {
    @GetMapping("/video")
    @Operation(summary = "获取视频", description = "视频获取")
    public void list(){
        MediaVideoTransfer mediaVideoTransfer = new MediaVideoTransfer();
        mediaVideoTransfer.setSrc("rtsp://localhost:554/live");
        mediaVideoTransfer.setTarget("rtmp://localhost:1935/myapp/yngg");
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
}


