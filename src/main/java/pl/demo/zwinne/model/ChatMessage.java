package pl.demo.zwinne.model;


import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "CHAT_MESSAGE")
@Data
public class ChatMessage {
    @Id
    @GeneratedValue
    @Column(name="ID")
    private Long id;

    @Column(name = "MESSAGE", nullable = false, length = 256)
    private String message;

    @ManyToOne
    @JoinColumn(name = "USER_ID", nullable = false)
    private User user;
}
