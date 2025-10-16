package org.example.upload.controller;


import org.example.upload.service.MinioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

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
                                   @RequestParam(value = "groupName", required = false) String groupName,
                                   @RequestParam("file") MultipartFile file,
                                   Model model) {
        try {
            if (groupNumber == null || groupNumber.trim().isEmpty()) {
                model.addAttribute("messageType", "danger");
                model.addAttribute("message", "组号不能为空");
                return "upload";
            }

            if (file == null || file.isEmpty()) {
                model.addAttribute("messageType", "danger");
                model.addAttribute("message", "请选择要上传的文件");
                return "upload";
            }

            String originalFilename = file.getOriginalFilename();
            String fileExtension = "";
            if (originalFilename != null && originalFilename.contains(".")) {
                fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
            }

            // 类型白名单校验（可按需要扩展）
            Set<String> allowed = new HashSet<>(Arrays.asList(
                    "zip","pdf","doc","docx","ppt","pptx","xls","xlsx","png","jpg","jpeg","txt"
            ));
            String extNoDot = fileExtension.startsWith(".") ? fileExtension.substring(1) : fileExtension;
            if (extNoDot.isEmpty() || !allowed.contains(extNoDot.toLowerCase())) {
                model.addAttribute("messageType", "danger");
                model.addAttribute("message", "不支持的文件类型，请上传: " + allowed);
                return "upload";
            }

            String fileName = groupNumber + fileExtension;

            // 上传文件到默认桶
            minioService.uploadFile(fileName, file);

            model.addAttribute("messageType", "success");
            String namePart = (groupName != null && !groupName.trim().isEmpty()) ? ("（" + groupName.trim() + "）") : "";
            model.addAttribute("message", "文件上传成功！组号" + namePart + ": " + groupNumber + "，文件名: " + fileName);
        } catch (Exception e) {
            model.addAttribute("messageType", "danger");
            model.addAttribute("message", "文件上传失败: " + e.getMessage());
        }
        return "upload";
    }
}
