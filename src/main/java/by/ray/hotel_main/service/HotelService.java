package by.ray.hotel_main.service;

import by.ray.hotel_main.dto.HotelSearchCriteria;
import by.ray.hotel_main.model.Amenity;
import by.ray.hotel_main.model.Hotel;

import java.util.List;
import java.util.Map;

public interface HotelService {
    List<Hotel> findHotels();
    Hotel findHotel(Integer id);
    Hotel saveHotel(Hotel hotel);
    Hotel saveHotelAmenities(Integer id, List<Amenity> amenities);
    List<Hotel> searchHotel(HotelSearchCriteria hotelSearchCriteria);
    Map<String, Long> getHistogram(String param);
}
