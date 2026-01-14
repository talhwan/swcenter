package com.thc.sprbasic2025.controller;

import com.thc.sprbasic2025.util.FileUpload;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RequestMapping("/api/default") //모든 메서드에 공통적으로 앞에 붙는 주소값을 넣어줄 수 있습니다!!
@RestController
public class DefaultRestController {

    @PostMapping("/uploadFile")
    public String uploadFile(MultipartFile file) throws IOException {
        return FileUpload.upload(file);
    }

}
