package pl.demo.zwinne.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "TASK")
@Data
public class Task {

    @Id
    @GeneratedValue
    @Column(name="ID")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "PROJECT_ID", nullable = false)
    private Project project;

    @Column(name = "NAME", nullable = false, length = 50)
    private String name;

    @Column(name = "\"ORDER\"", nullable = false)
    private int order;

    @Column(name = "DESCRIPTION", nullable = false, length = 1000)
    private String description;

    @Column(name = "ESTIMATED_TIME", nullable = false)
    private int estimatedTime;

}
