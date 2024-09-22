package com.example.entity;

import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class ChatConfig {

    private Long chatId;
    private boolean isBlockPremiumEmoji;
    private boolean enableAllowedPremiumEmojiSets;
    private List<String> allowedPremiumEmojiSets;
}
