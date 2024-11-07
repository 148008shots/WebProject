package com.hh.controller;

import com.hh.pojo.Result;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.UUID;
@RestController
@RequestMapping("/common")
public class ImgController {
    @Value("${images.path}")
    private String pictureUrl;

    //    单文件上传
    @PostMapping("/imgUpload")
    public Result<String> upload(MultipartFile file) {
        //获取原始文件名
        String originalFilename = file.getOriginalFilename();
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));

        //使用uuid生成一个新的文件名
        String fileName = UUID.randomUUID().toString() + suffix;
        //创建目录,判断目录是否存在
        File dir = new File(pictureUrl);
        //判断当前目录是否存在，不存在则创建目录
        if (!dir.exists()){
            //如果不存在则创建
            dir.mkdirs();
        }
        try {
            //将临时文件转存到指定位置
            file.transferTo(new File(pictureUrl+fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        //注意src是前端服务器的根目录
        return Result.success("http://localhost:5173/src/assets/uploadImg/"+fileName);
    }

    /**
     * 文件下载
     * @param name
     * @param response
     */
    @GetMapping("/download")
    public void download(String name, HttpServletResponse response){
        try {
            //输入流，读取文件内容
            FileInputStream fileInputStream = new FileInputStream(new File(pictureUrl + name));
            //输出流，将文件返回给浏览器，在浏览器中展示图片
            ServletOutputStream outputStream = response.getOutputStream();
            response.setContentType("image/jpeg");

            int len = 0;
            byte[] bytes = new byte[1024];
            while ((len = fileInputStream.read(bytes)) != -1){
                outputStream.write(bytes,0,len);
                outputStream.flush();
            }
            //关闭 资源
            outputStream.close();
            fileInputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
