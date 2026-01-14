package com.thc.sprbasic2025.util;

import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Date;

public class FileUpload {
    public static String path = "C:/workspace/uploadfiles/spradv2025summer/";
    public static String upload(MultipartFile file) throws IOException {
        String finalFileName = null;
        if(file != null){
            String fileName = file.getOriginalFilename();
            File newfile = new File(path);
            if(!newfile.exists()) {
                newfile.mkdirs();
            }
            Date date = new Date();
            String temp_date = date.getTime() + "";
            FileCopyUtils.copy(file.getBytes(), new File(path + temp_date + "_" + fileName));
            finalFileName = temp_date + "_" + fileName;
        }
        return finalFileName;
    }
}
