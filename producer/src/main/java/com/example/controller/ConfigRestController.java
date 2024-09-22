package com.example.controller;

import com.example.entity.ChatConfig;
import com.example.service.ChatConfigService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/chat-config")
@RequiredArgsConstructor
public class ConfigRestController {

    private final ChatConfigService chatConfigService;


    @PostMapping
    public ResponseEntity<ChatConfig> createOrUpdateChatConfig(@RequestBody ChatConfig chatConfig) {
        log.info("Config updated");
        chatConfigService.updateChatConfig(chatConfig);
        return ResponseEntity.ok(chatConfig);
    }

    @GetMapping("/{chatId}")
    public ResponseEntity<ChatConfig> getChatConfig(@PathVariable Long chatId) {
        ChatConfig config = chatConfigService.getChatConfig(chatId);
        if (config != null) {
            return ResponseEntity.ok(config);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
