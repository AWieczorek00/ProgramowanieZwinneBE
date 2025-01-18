package pl.demo.zwinne.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;


@Entity
@Table(name = "FILE")
@Data
public class File {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String filename;

    @Column(nullable = false,  length = 1000)
    private String url;

    @ManyToOne
    @JoinColumn(nullable = false)
    @JsonIgnore
    private Project project;

    public File(){}

    public File(String filename, String url){
        this.filename = filename;
        this.url = url;
    }

}
