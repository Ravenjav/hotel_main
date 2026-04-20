package by.ray.hotel_main.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
@Embeddable
public class ArrivalTime {
    @Pattern(regexp = "^(?:[01]\\d|2[0-3]):[0-5]\\d$", message = "checkIn time must match (00:00-23:59) pattern")
    @Column(name = "check_in")
    private String checkIn;
    @Pattern(regexp = "^(?:[01]\\d|2[0-3]):[0-5]\\d$", message = "checkOut time must match (00:00-23:59) pattern")
    @Column(name = "check_out")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String checkOut;
}

