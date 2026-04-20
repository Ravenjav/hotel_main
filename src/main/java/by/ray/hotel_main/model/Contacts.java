package by.ray.hotel_main.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.Email;
import lombok.Data;

@Data
@Embeddable
public class Contacts {
    @Column(name = "phone")
    private String phone;
    @Email(message = "Email should be valid")
    @Column(name = "email")
    private String email;
}
