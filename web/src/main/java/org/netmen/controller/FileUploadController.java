package org.netmen.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@RestController
@Slf4j
@Tag(name = "FileUpload")
public class FileUploadController {

    @PostMapping("/upload")
    @Operation(summary = "文件上传", description = "详细描述")
    public String upload(MultipartFile file) throws IOException {
        String originalFilename = file.getOriginalFilename();
        file.transferTo(new File("D:\\code\\net_recruitment\\files\\" + originalFilename));
        return "D:\\code\\net_recruitment\\files\\" + originalFilename;
    }

}
