package by.ray.hotel_main.dto;

import jakarta.validation.constraints.AssertTrue;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HotelSearchCriteria {
    private String name;
    private String brand;
    private String city;
    private String country;
    private List<String> amenities;

    @AssertTrue(message = "At least one search criteria must be provided")
    private boolean isAtLeastOneCriteriaProvided() {
        return name != null || brand != null || city != null || country != null || (amenities != null && !amenities.isEmpty());
    }
}
