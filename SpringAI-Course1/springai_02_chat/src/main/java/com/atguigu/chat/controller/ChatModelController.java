package com.atguigu.chat.controller;

import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.model.Generation;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class ChatModelController {
    @Autowired
    private ChatModel chatModel;

    @GetMapping("/chatModel01")
    public String chatModel01(@RequestParam("message") String message) {
        // 使用带有String参数的call方法
        String result = chatModel.call(message);
        return result;
    }

    @GetMapping("/chatModel02")
    public String chatModel02(@RequestParam("message") String message) {
        // 使用带有Prompt参数的call方法
        ChatResponse chatResponse = chatModel.call(
                new Prompt(message,
                        ChatOptions.builder()
                                .model("deepseek-chat")
                                .temperature(0.7)
                                .build()
                )
        );
        String text = chatResponse.getResult().getOutput().getText();
        return text;
    }

    /**
     * 提示词操作
     * @param name 名字
     * @param voice 习惯
     * @return
     */
    @GetMapping("/prompt")
    public String prompt(@RequestParam("name") String name, @RequestParam("voice") String voice) {
        // 设置用户输入信息
        String userText = "给我推荐四川的至少三种美食";
        UserMessage userMessage = new UserMessage(userText);

        // 设置系统提示信息
        String systemText = """
                你是一个美食咨询助手，可以帮助人们查询美食信息。
                你的名字是{name},
                你应该用你的名字和{voice}的饮食习惯回复用户的请求。
                """;

        // 使用Prompt Template设置信息
        SystemPromptTemplate systemPromptTemplate = new SystemPromptTemplate(systemText);

        // 替换占位符
        Message systemMessage = systemPromptTemplate.createMessage(Map.of("name", name, "voice", voice));

        // 使用Prompt封装
        Prompt prompt = new Prompt(List.of(userMessage, systemMessage));

        // 调用chatModel方法
        ChatResponse chatResponse = chatModel.call(prompt);

        List<Generation> results = chatResponse.getResults();
        return results.stream().map(x -> x.getOutput().getText()).collect(Collectors.joining(""));
    }
}
