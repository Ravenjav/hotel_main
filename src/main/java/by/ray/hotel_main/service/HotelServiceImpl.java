package by.ray.hotel_main.service;

import by.ray.hotel_main.dto.HotelSearchCriteria;
import by.ray.hotel_main.exception.HotelNotFoundException;
import by.ray.hotel_main.model.Amenity;
import by.ray.hotel_main.model.HistogramResult;
import by.ray.hotel_main.model.Hotel;
import by.ray.hotel_main.repository.AmenityRepository;
import by.ray.hotel_main.repository.HotelRepository;
import by.ray.hotel_main.specification.HotelSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HotelServiceImpl implements HotelService {
    private final HotelRepository hotelRepository;
    private final AmenityRepository amenityRepository;

    @Override
    public List<Hotel> findHotels() {
        return hotelRepository.findAll();
    }

    @Override
    public Hotel findHotel(Integer id) {
        return hotelRepository.findById(id).orElseThrow(() -> new HotelNotFoundException(id));
    }

    @Override
    public Hotel saveHotel(Hotel hotel) {
        return hotelRepository.save(hotel);
    }

    @Override
    public Hotel saveHotelAmenities(Integer id, List<Amenity> amenities) {
        Hotel hotel = hotelRepository.findById(id).orElseThrow(() -> new HotelNotFoundException(id));

        Set<String> amenityNames = amenities.stream()
                .map(Amenity::getName)
                .collect(Collectors.toSet());
        List<Amenity> existingAmenities = amenityRepository.findByNameIn(amenityNames);
        Map<String, Amenity> existingAmenitiesMap = existingAmenities.stream()
                .collect(Collectors.toMap(Amenity::getName, Function.identity()));
        List<Amenity> updatedAmenities = amenities.stream()
                .map(amenity -> existingAmenitiesMap.computeIfAbsent(
                        amenity.getName(),
                        name -> amenityRepository.save(amenity)))
                .collect(Collectors.toList());
        hotel.setAmenities(updatedAmenities);
        return hotelRepository.save(hotel);
    }

    @Override
    public List<Hotel> searchHotel(HotelSearchCriteria hotelSearchCriteria) {
        HotelSpecification hotelSpecification = new HotelSpecification(hotelSearchCriteria);
        return hotelRepository.findAll(hotelSpecification);
    }

    @Override
    public Map<String, Long> getHistogram(String param) {
        List<HistogramResult> histogramResults;
        switch (param) {
            case "city": {
                histogramResults = hotelRepository.countByAddressCity();
                break;
            }
            case "brand": {
                histogramResults = hotelRepository.countByBrand();
                break;
            }
            case "country": {
                histogramResults = hotelRepository.countByAddressCountry();
                break;
            }
            case "amenities": {
                histogramResults = hotelRepository.countByAmenities();
                break;
            }
            default:
                throw new IllegalArgumentException(String.format("Unknown argument %s", param));
        }
        return histogramResults.stream().collect(Collectors.toMap(HistogramResult::getKey, HistogramResult::getNumber));
    }
}