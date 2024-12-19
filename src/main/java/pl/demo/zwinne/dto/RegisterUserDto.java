package pl.demo.zwinne.dto;

import lombok.Data;

@Data
public class RegisterUserDto {

    private String name;

    private String surname;

    private String email;

    private String password;

    private String indexNumber;

    private Boolean stationary;

    private String role;

}
