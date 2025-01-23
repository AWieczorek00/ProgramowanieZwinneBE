package pl.demo.zwinne.IntegrationTests;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import pl.demo.zwinne.model.ChatMessage;
import pl.demo.zwinne.model.ChatMessageForm;
import pl.demo.zwinne.respository.ChatRepository;
import pl.demo.zwinne.service.ChatService;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class ChatControllerTest {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private ChatService chatService;
    @Autowired
    private ChatRepository chatRepository;

    ChatMessageForm chatMessageForm = new ChatMessageForm();

    @BeforeEach
    public void setUp(){
        chatMessageForm.setMessage("Testowe");
        chatMessageForm.setUsername("test@gmail.com");
        chatMessageForm.setUserId(1L);
        chatService.save(chatMessageForm);
    }

    @AfterEach
    public void clean(){
        ChatMessage chatMessage = chatService.getAll().get(0);
        chatRepository.delete(chatMessage);
    }

    @Test
    @WithMockUser(username = "test", roles = {"STUDENT"})
    public void getAllChatMessagesAsStudent() throws Exception {
        mvc.perform(get("/api/globalChat/")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].message", is(chatMessageForm.getMessage())));
    }

    @Test
    @WithMockUser(username = "test", roles = {"ADMIN"})
    public void getAllChatMessagesAsAdmin() throws Exception {
        mvc.perform(get("/api/globalChat/")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].message", is(chatMessageForm.getMessage())));
    }

    @Test
    @WithMockUser(username = "test", roles = {"SUPER_ADMIN"})
    public void getAllChatMessagesAsSuperAdmin() throws Exception {
        mvc.perform(get("/api/globalChat/")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].message", is(chatMessageForm.getMessage())));
    }

    @Test
    @WithMockUser(username = "test", roles = {"TEACHER"})
    public void getAllChatMessagesAsTeacher() throws Exception {
        mvc.perform(get("/api/globalChat/")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].message", is(chatMessageForm.getMessage())));
    }

    @Test
    public void getAllChatMessagesAsGuest() throws Exception {
        mvc.perform(get("/api/globalChat/")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

}
