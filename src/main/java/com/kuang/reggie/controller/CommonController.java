package com.kuang.reggie.controller;

import com.kuang.reggie.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.UUID;

/**
 * 用于文件上传和下载
 */
@Slf4j
@RestController
@RequestMapping("/common")
public class CommonController {

    //文件上传的到的磁盘的位置
    @Value("${reggie.path}")
    private String filePath;

    //文件的上传
    //http://localhost:8080/common/upload
    @PostMapping("/upload")
    public R<String> upload(MultipartFile file) throws IOException {
        log.info("上传到磁盘中的位置为{}" + filePath);
        log.info("上传的文件名为{}" + file.getOriginalFilename());
        String originalFilename = file.getOriginalFilename();  //获取上传的全文件名称
        String imgSuffix = originalFilename.substring(originalFilename.lastIndexOf(".")); //获取到.xxx
        String uuid = UUID.randomUUID().toString();   //产生一个uuid作为文件名
        String fileName = uuid + imgSuffix;   //最终带后缀的文件名
        //判断存到到磁盘的文件是否文件。如果不存在这创建出来
        File file1 = new File(fileName);
        if(!file1.exists()){
            file1.mkdirs();
        }

        file.transferTo(new File(filePath+fileName));
        //返回文件名
        return R.success(fileName);
    }


    //文件下载
    //http://localhost:8080/common/download?name=54c9fd75-8ac2-4fe5-93c3-5d0be43b0522.png
    @GetMapping("/download")
    public void download(String name, HttpServletResponse response) throws Exception {
        FileInputStream fileInputStream = new FileInputStream(filePath + name);
        ServletOutputStream outputStream = response.getOutputStream();
        int available = fileInputStream.available();
        byte[] bytes = new byte[available];
        fileInputStream.read(bytes);
        outputStream.write(bytes);
    }
}
