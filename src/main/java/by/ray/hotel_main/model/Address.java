package by.ray.hotel_main.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Embeddable
@AllArgsConstructor
@NoArgsConstructor
public class Address {
    @Column(name = "house_number")
    private int houseNumber;
    @Column(name = "street")
    private String street;
    @Column(name = "city")
    private String city;
    @Column(name = "country")
    private String country;
    @Pattern(regexp = "^\\d{5,10}$", message = "Post code must consist of numbers")
    @Column(name = "post_code")
    private String postCode;
}
