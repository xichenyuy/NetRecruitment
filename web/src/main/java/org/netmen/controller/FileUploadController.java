package org.netmen.controller;

import org.netmen.common.response.Result;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

@RestController
public class FileUploadController {
    @PostMapping("/upload")
    public Result<String> upload(MultipartFile file) throws IOException {
        //把文件存储到本地磁盘
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        String originalFilename = file.getOriginalFilename();
        //设置文件名为 当前时间+uuid
        String fileName = sdf.format(new Date()) + UUID.randomUUID().toString() + originalFilename.substring(originalFilename.lastIndexOf("."));
        file.transferTo(new File("C:\\NetRecruitmentFile\\" + fileName));
        return Result.success("C:\\NetRecruitmentFile\\" + fileName);


    }
    /**
     * 还有可以优化的点
     * 比如应该记录用户上传文件的IP 防止有恶意上传文件
     * 文件上传后但报名表单没有正确提交，就会有没被使用作头像的多余文件
     * 应该着重完善整个上传的体系，包括永固身份，上传文件的类型大小，重复上传后删除旧文件之类的
     */
}
