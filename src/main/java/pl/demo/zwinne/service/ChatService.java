package pl.demo.zwinne.service;

import pl.demo.zwinne.model.ChatMessage;
import pl.demo.zwinne.model.ChatMessageForm;

import java.util.List;

public interface ChatService {

    List<ChatMessage> getAll();
    ChatMessage save(ChatMessageForm chatMessageForm);
}
