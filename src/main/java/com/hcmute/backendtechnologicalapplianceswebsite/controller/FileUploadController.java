package com.hcmute.backendtechnologicalapplianceswebsite.controller;

import com.hcmute.backendtechnologicalapplianceswebsite.utils.fileUtils.upload.FileUploadUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;

@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:4200"})
@RestController
@RequestMapping("/api/technological_appliances/")
public class FileUploadController {
    @PostMapping("/uploadFile")
    public ResponseEntity<String> uploadFile(@RequestParam("files") MultipartFile[] files) {

        StringBuilder ImgName = new StringBuilder();
        for (var file : files) {
            String tempName = GetFileNameImg(file);
            ImgName.append(tempName).append("//");
        }
        return ResponseEntity.ok().body(ImgName.toString());
    }

    private String GetFileNameImg(MultipartFile file) {
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        String fileDownloadUri = "http://localhost:8080/downloadFile/" + fileName;
        long size = file.getSize();

        FileUploadUtil.saveFile(fileName, file);

        return fileName;
    }
}
