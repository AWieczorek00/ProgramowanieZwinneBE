package pl.demo.zwinne.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.demo.zwinne.model.ChatMessage;
import pl.demo.zwinne.service.ChatService;

import java.util.List;

@Controller
@Slf4j
@RequestMapping("/api/globalChat")
@PreAuthorize("isAuthenticated()")
public class ChatController {

    @Autowired
    private ChatService chatService;

    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN','TEACHER', 'STUDENT')")
    @GetMapping("/")
    public ResponseEntity<List<ChatMessage>> getAllGlobalMessages(){
        return ResponseEntity.ok(chatService.getAll());
    }

}
