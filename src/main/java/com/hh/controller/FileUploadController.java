package com.hh.controller;

import com.hh.pojo.Result;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@RestController
public class FileUploadController {
    @PostMapping("/upload")
    public Result<String> upload(MultipartFile file) throws IOException {
        //把文件存储到本地磁盘
        String originalFilename = file.getOriginalFilename();
        //保证文件名唯一
        String fileName = UUID.randomUUID().toString()+originalFilename.substring(originalFilename.lastIndexOf("."));

        file.transferTo(new File("D:\\html\\big-event\\src\\main\\resources\\img\\img1"+fileName));

        return Result.success("Url访问地址！");
    }
}
