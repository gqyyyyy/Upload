package org.example.upload.controller;


import org.example.upload.service.MinioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class FileUploadController {
    @Autowired
    private MinioService minioService;

    @GetMapping("/")
    public String showUploadForm(Model model) {
        return "upload";
    }

    @PostMapping("/upload")
    public String handleFileUpload(@RequestParam("groupNumber") String groupNumber,
                                   @RequestParam("file") MultipartFile file,
                                   Model model) {
        try {
            if (file.isEmpty()) {
                model.addAttribute("message", "请选择要上传的文件");
                return "upload";
            }

            //获取文件原始扩展名
            String originalFilename = file.getOriginalFilename();
            String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
            // 构建上传到MinIO的文件名：组号+原始扩展名
            String fileName = groupNumber + fileExtension;

            // 上传文件到MinIO
            minioService.uploadFile("assign1", fileName, file);

            model.addAttribute("message", "文件上传成功！文件名: " + fileName);
        } catch (Exception e) {
            model.addAttribute("message", "文件上传失败: " + e.getMessage());
        }
        return "upload";
    }
}
