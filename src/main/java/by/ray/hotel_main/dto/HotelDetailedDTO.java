package by.ray.hotel_main.dto;

import by.ray.hotel_main.model.Address;
import by.ray.hotel_main.model.ArrivalTime;
import by.ray.hotel_main.model.Contacts;
import lombok.Data;

import java.util.List;

@Data
public class HotelDetailedDTO {
    private int id;
    private String name;
    private String brand;
    private Address address;
    private Contacts contacts;
    private ArrivalTime arrivalTime;
    private List<String> amenities;
}
