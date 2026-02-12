package com.atguigu.other.controller;

import com.alibaba.cloud.ai.dashscope.audio.DashScopeAudioSpeechModel;
import com.alibaba.cloud.ai.dashscope.audio.DashScopeAudioSpeechOptions;
import org.springframework.ai.audio.tts.TextToSpeechPrompt;
import org.springframework.ai.audio.tts.TextToSpeechResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

@RestController
public class AudioModelController {
    @Autowired
    private DashScopeAudioSpeechModel dashScopeAudioSpeechModel;

    private static final String TEXT = "床前明月光，疑是地上霜。举头望明月，低头思故乡。";

    private static final String PATH = System.getProperty("user.dir") + "\\springai_06_image_audio\\src\\main\\resources\\audio";

    @GetMapping("/tts")
    public void tts() {
        // 使用构建器模式创建DashScopeAudioSpeechOptions实例并设置参数
        DashScopeAudioSpeechOptions options =
                DashScopeAudioSpeechOptions.builder()
                        .speed(1.0) // 语速：1.0为正常速度
                        .pitch(0.9) // 音调：0.9略低于正常音调
                        .volume(60) // 音量：60
                        .build();

        TextToSpeechResponse response = dashScopeAudioSpeechModel.call(
                new TextToSpeechPrompt(TEXT, options));

        File file = new File(PATH + "\\output.mp3");
        try (FileOutputStream fos = new FileOutputStream(file)) {
            byte[] output = response.getResult().getOutput();
            fos.write(output);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
