package org.netmen.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

@RestController
@Slf4j
@Tag(name = "FileUpload")
public class FileUploadController {

    @PostMapping("/upload")
    @Operation(summary = "文件上传", description = "详细描述")
    public String upload(MultipartFile file) throws IOException {
        String originalFilename = file.getOriginalFilename();
        String filename = UUID.randomUUID().toString() + originalFilename.substring(originalFilename.lastIndexOf("."));


        // 将MultipartFile转换为BufferedImage
        BufferedImage originalImage = ImageIO.read(file.getInputStream());

        // 获取图片的宽度和高度
        int width = originalImage.getWidth();
        int height = originalImage.getHeight();

        // 创建一个新的BufferedImage对象，大小和原始图片一样
        BufferedImage flippedImage = new BufferedImage(width, height, originalImage.getType());

        // 创建一个AffineTransform对象，并设置水平翻转的变换
        AffineTransform transform = new AffineTransform();
        transform.scale(-1.0, 1.0);
        transform.translate(-width, 0);

        // 使用AffineTransformOp应用变换
        AffineTransformOp op = new AffineTransformOp(transform, AffineTransformOp.TYPE_BILINEAR);
        op.filter(originalImage, flippedImage);

        file.transferTo(new File("D:\\code\\video\\video_demo\\public\\imageInput\\" + filename));
        ImageIO.write(flippedImage, "jpg", new File("D:\\code\\video\\video_demo\\public\\imageOutput\\" + filename));

        return filename;
    }
}
