package com.hcmute.backendtechnologicalapplianceswebsite.controller;

import com.hcmute.backendtechnologicalapplianceswebsite.fileUtils.upload.FileUploadResponse;
import com.hcmute.backendtechnologicalapplianceswebsite.fileUtils.upload.FileUploadUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/technological_appliances/")
public class FileUploadController {
    @PostMapping("/uploadFile")
    public ResponseEntity<FileUploadResponse> uploadFile(@RequestParam("file") MultipartFile file) {
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        String fileDownloadUri = "http://localhost:8080/downloadFile/" + fileName;
        long size = file.getSize();

        FileUploadUtil.saveFile(fileName, file);

        FileUploadResponse fileUploadResponse = new FileUploadResponse(fileName, fileDownloadUri, size);
        return ResponseEntity.ok().body(fileUploadResponse);
    }
}
