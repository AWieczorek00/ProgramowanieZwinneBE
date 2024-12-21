package pl.demo.zwinne.websocket;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import pl.demo.zwinne.model.ChatMessage;
import pl.demo.zwinne.model.ChatMessageForm;
import pl.demo.zwinne.service.ChatService;

@Controller
@Slf4j
public class WebSocketController {

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate; //to pod przyszłe priv chaty, jak zdążę :)
    @Autowired
    private ChatService chatService;

    @MessageMapping("/globalChat")
    @SendTo("/topic/global")
    public ChatMessage globalChat(ChatMessageForm chatMessageForm) throws Exception{
        return chatService.save(chatMessageForm);
    }
}
