package com.hcmute.backendtechnologicalapplianceswebsite.controller;

import com.hcmute.backendtechnologicalapplianceswebsite.fileUtils.dowload.FileDowloadUtil;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/technological_appliances/")
public class FileDownloadController {
    @GetMapping("dowloadFile/{fileCode}")
    public ResponseEntity<?> dowloadFile(@PathVariable("fileCode") String fileCode)
    {
        FileDowloadUtil dowloadUtil=new FileDowloadUtil();
        Resource resource=null;
        try {
            resource=dowloadUtil.getFileAsResource(fileCode);
        }catch (IOException e){
            e.printStackTrace();
            return  ResponseEntity.internalServerError().build();
        }
        if(resource==null){
            return new ResponseEntity<>("File not found", HttpStatus.NOT_FOUND);
        }
        String contenttype="application/octet-stream";
        String headerValue="attachment; filename=\""+resource.getFilename()+"\"";
        return ResponseEntity.ok().contentType(MediaType.parseMediaType(contenttype)).header(HttpHeaders.CONTENT_DISPOSITION,headerValue)
                .body(resource);
    }
}
