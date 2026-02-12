package com.atguigu.other.controller;

import com.alibaba.cloud.ai.dashscope.api.DashScopeImageApi;
import com.alibaba.cloud.ai.dashscope.image.DashScopeImageModel;
import com.alibaba.cloud.ai.dashscope.image.DashScopeImageOptions;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.ai.image.ImagePrompt;
import org.springframework.ai.image.ImageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.InputStream;
import java.net.URI;
import java.net.URL;

@RestController
public class ImageModelController {
    @Autowired
    private DashScopeImageModel dashScopeImageModel;

    @GetMapping("/image")
    public void getImage(@RequestParam(value = "message", defaultValue = "生成一只小猫") String message, HttpServletResponse response) {
        /*ImageResponse imageResponse = dashScopeImageModel.call(
                new ImagePrompt(
                        message,
                        DashScopeImageOptions.builder()
                                .model(DashScopeImageApi.DEFAULT_IMAGE_MODEL)
                                .n(1) // 要生成的图像数。必须介于 1 和 10 之间
                                .height(1024) // 生成的图像的高宽度
                                .width(1024).build()
                )
        );*/
        ImageResponse imageResponse = dashScopeImageModel.call(new ImagePrompt(message));
        // 获取生成图像的地址
        String imageUrl = imageResponse.getResult().getOutput().getUrl();
        try {
            // 使用输出流在浏览器输出
            URL url = URI.create(imageUrl).toURL();
            InputStream in = url.openStream();
            response.setHeader("Content-Type", MediaType.IMAGE_PNG_VALUE);
            response.getOutputStream().write(in.readAllBytes());
            response.getOutputStream().flush();
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}
