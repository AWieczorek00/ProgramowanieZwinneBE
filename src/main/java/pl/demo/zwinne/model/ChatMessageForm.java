package pl.demo.zwinne.model;

import lombok.Data;

@Data
public class ChatMessageForm {
    private String message;
    private String username;
    private Long userId;
}
