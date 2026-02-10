package com.atguigu.chat.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
public class ChatRoleController {
    @Autowired
    private ChatClient chatClient;

    // 角色预设，使用非流式响应
    @GetMapping("/chatRole")
    public String chatRole(@RequestParam(value = "message") String message) {
        return chatClient.prompt().user(message).call().content();
    }

    // 角色预设，使用流式响应
    @GetMapping(value = "/chatRoleStream", produces = "text/html;charset=UTF-8")
    public Flux<String> chatRoleStream(@RequestParam(value = "message") String message) {
        return chatClient.prompt().user(message).stream().content();
    }
}
