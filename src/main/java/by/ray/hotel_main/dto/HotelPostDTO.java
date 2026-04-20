package by.ray.hotel_main.dto;

import by.ray.hotel_main.model.Address;
import by.ray.hotel_main.model.ArrivalTime;
import by.ray.hotel_main.model.Contacts;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HotelPostDTO {
    @NotEmpty(message = "Name cannot be empty string")
    @Size(max = 100, message = "Name cannot be longer than 100 characters")
    private String name;
    @NotEmpty(message = "Description cannot be empty string")
    @Size(max = 255, message = "Description cannot be longer than 255 characters")
    private String description;
    @Size(max = 100, message = "Brand cannot be longer than 100 characters")
    private String brand;
    @NotNull(message = "Address cannot be null")
    @Valid
    private Address address;
    @NotNull(message = "Contacts cannot be null")
    @Valid
    private Contacts contacts;
    @NotNull(message = "Arrival time cannot be null")
    @Valid
    private ArrivalTime arrivalTime;
}
