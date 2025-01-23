package pl.demo.zwinne.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import pl.demo.zwinne.dto.ProjectForm;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "PROJECT")
@Data
public class Project {

    @Id
    @GeneratedValue
    @Column(name = "ID")
    private Long id;

    @NotNull(message = "Nazwa nie może być pusta")
    @NotEmpty(message = "Nazwa nie może być pusta")
    @Column(name = "NAME", nullable = false, length = 50)
    private String name;

    @CreationTimestamp
    @Column(name = "DATE_CREATE", nullable = false, updatable = false)
    private LocalDateTime dateCreate;

    @Column(name = "DESCRIPTION", length = 500)
    private String description;

    @Column(name = "DATE_DEFENSE", nullable = false)
    private LocalDateTime dateDefense;

    @OneToMany(mappedBy = "project",  cascade = CascadeType.REMOVE)
    private List<Task> tasks = new ArrayList<>();

    @OneToMany(mappedBy = "project", cascade = CascadeType.REMOVE)
    private List<File> file = new ArrayList<>();

    @ElementCollection
    private List<User> users = new ArrayList<>();

    public Project(ProjectForm projectForm){
        this.name = projectForm.getName();
        this.description = projectForm.getDescription();
        this.dateCreate = projectForm.getDateCreate();
        this.dateDefense = projectForm.getDateDefense();
    }

    public Project(){}
}
