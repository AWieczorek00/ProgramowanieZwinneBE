package pl.demo.zwinne.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class RegisterUserDto {

    @NotNull(message = "Imię nie może być puste")
    @NotEmpty(message = "Imię nie może być puste")
    private String name;

    @NotNull(message = "Nazwisko nie może być puste")
    @NotEmpty(message = "Nazwisko nie może być puste")
    private String surname;

    @NotNull(message = "Email nie może być pusty")
    @NotEmpty(message = "Email nie może być pusty")
    private String email;

    @Size(min = 8, max = 128)
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=.])(?=\\S+$).{8,}$",
            message = "Hasło musi zawierać jedną cyfrę, jeden mały znak, jeden duży znak, jeden specjalny znak, i brak spacji")
    private String password;

    @Size(min = 6, max = 20, message = "Index musi być długości od 6 do 20 znaków")
    private String indexNumber;

    private Boolean stationary;

}
