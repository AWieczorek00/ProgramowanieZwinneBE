package pl.demo.zwinne.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Entity
@Table(name = "TASK")
@Data
public class Task {

    @Id
    @GeneratedValue
    @Column(name="ID")
    private Long id;

    @NotEmpty(message = "Nazwa nie może być pusta")
    @NotNull(message = "Nazwa nie może być pusta")
    @Column(name = "NAME", nullable = false, length = 50)
    private String name;

    @Column(name = "\"ORDER\"", nullable = false)
    private int order;

    @Column(name = "DESCRIPTION", nullable = false, length = 1000)
    private String description;

    @Column(name = "ESTIMATED_TIME", nullable = false)
    private int estimatedTime;

    @ManyToOne
    @JoinColumn(name = "PROJECT_ID", nullable = false)
    @JsonIgnore
    private Project project;
}
