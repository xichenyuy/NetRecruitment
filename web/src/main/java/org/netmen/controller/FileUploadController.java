package org.netmen.controller;

import com.mysql.cj.xdevapi.JsonArray;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.util.Base64;
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

    @GetMapping("/predict")
    public ResponseEntity<String> predict(@RequestParam String imagePath){
        System.out.println(imagePath);
        // 注意：这里假设imagePath是有效的，并且已经过适当的验证和清理
        String pythonScriptPath = "D:/code/yolov8/detect.py";
        ProcessBuilder processBuilder = new ProcessBuilder(
                "D:\\dev\\miniconda3\\envs\\py38yolo\\python.exe", pythonScriptPath, imagePath
        );
        processBuilder.redirectErrorStream(true);

        int exitCode = 0;
        try {
            Process process = processBuilder.start();
            // 使用try-with-resources来确保流被正确关闭
            try(InputStream inputStream = process.getInputStream();
                ByteArrayOutputStream baos = new ByteArrayOutputStream()){
                // 从Python脚本的标准输出中读取字节数据
                byte[] buffer = new byte[1024];
                int bytesRead;

                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    baos.write(buffer, 0, bytesRead);
                }
                exitCode = process.waitFor();
                if (exitCode == 0) {  // 脚本成功执行
                    // 这里处理baos中的数据
                    byte[] imageData = baos.toByteArray();
                    // 返回Base64编码的图像数据 也可在此上传到OSS服务器
//                    String base64Image = Base64.getEncoder().encodeToString(imageData);
//                    return ResponseEntity.ok(base64Image);
                    try (ByteArrayInputStream bais = new ByteArrayInputStream(imageData)) {
                        // 解码图片
                        BufferedImage image = ImageIO.read(bais);

                        // 保存图片到文件
                        File outputfile = new File("D:\\code\\output.png");
                        ImageIO.write(image, "png", outputfile);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return ResponseEntity.ok("图片已保存为 output.png");
                } else {
                    // 脚本执行出错
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error occurred while executing the script");
                }
            }// 当try块结束时，inputStream和baos将被自动关闭
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred while executing the script");
        }
    }

    @GetMapping("/predict-new")
    public ResponseEntity<String> predictNew(@RequestParam String imagePath){
        System.out.println(imagePath);
        // 注意：这里假设imagePath是有效的，并且已经过适当的验证和清理
        String pythonScriptPath = "D:/code/yolov8/detect.py";
        ProcessBuilder processBuilder = new ProcessBuilder(
                "D:\\dev\\miniconda3\\envs\\py38yolo\\python.exe", pythonScriptPath, imagePath
        );
        processBuilder.redirectErrorStream(true);

        try {
            Process process = processBuilder.start();
            // 使用try-with-resources来确保流被正确关闭
            try (InputStream inputStream = process.getInputStream()) {
                byte[] imageData = inputStream.readAllBytes(); // Java 9+ 方法，或者您可以使用循环读取

                int exitCode = process.waitFor();
                if (exitCode == 0) {  // 脚本成功执行
                    // 您可以选择保存图像到文件或返回Base64编码的图像数据
                    // 保存图片到文件
                    File outputfile = new File("D:\\code\\output.png");
                    Files.write(outputfile.toPath(), imageData); // 使用Java NIO的Files类写入文件

                    // 如果需要返回Base64编码的图像数据
                    // String base64Image = Base64.getEncoder().encodeToString(imageData);
                    // return ResponseEntity.ok(base64Image);

                    return ResponseEntity.ok("图片已保存为 output.png");
                } else {
                    // 脚本执行出错
                    // 读取错误流以获取错误信息（如果需要）
                    try (InputStream errorStream = process.getErrorStream()) {
                        String errorMessage = new String(errorStream.readAllBytes());
                        // 处理或记录错误信息
                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Python 脚本执行出错: " + errorMessage);
                    }
                }
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred while executing the script");
        }
    }
}
