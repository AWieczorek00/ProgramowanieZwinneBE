package pl.demo.zwinne.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.demo.zwinne.model.ChatMessage;
import pl.demo.zwinne.model.ChatMessageForm;
import pl.demo.zwinne.model.User;
import pl.demo.zwinne.respository.ChatRepository;

import java.util.List;

@Service
public class ChatServiceImpl implements ChatService {

    @Autowired
    private ChatRepository chatRepository;
    @Autowired
    private UserService userService;

    @Override
    public List<ChatMessage> getAll() {
        return chatRepository.findAll();
    }

    @Override
    public ChatMessage save(ChatMessageForm chatMessageForm) {

        User user = userService.getUserById(chatMessageForm.getUserId());

        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setMessage(chatMessageForm.getMessage());
        chatMessage.setUsername(user.getUsername());
        chatMessage.setUser(user);

        return chatRepository.save(chatMessage);
    }

}
