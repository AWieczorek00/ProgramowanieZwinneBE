package pl.demo.zwinne.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "ADM_USER")
@Data
public class User {

    @Id
    @GeneratedValue
    @Column(name="ID")
    private Long id;

    @Column(name = "NAME", nullable = false, length = 50)
    private String name;

    @Column(name = "SURNAME", nullable = false, length = 100)
    private String surname;

    @Column(name = "EMAIL", nullable = false, length = 50)
    private String email;

    @Column(name = "INDEX_NUMBER", nullable = false, length = 20)
    private String indexNumber;

    @Column(name = "STATIONARY", nullable = false)
    private boolean stationary;
}
