package by.ray.hotel_main.service;

import by.ray.hotel_main.dto.HotelSearchCriteria;
import by.ray.hotel_main.exception.HotelNotFoundException;
import by.ray.hotel_main.model.*;
import by.ray.hotel_main.repository.AmenityRepository;
import by.ray.hotel_main.repository.HotelRepository;
import by.ray.hotel_main.specification.HotelSpecification;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anySet;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class HotelServiceImplTest {

    @Mock
    HotelRepository hotelRepository;
    @Mock
    AmenityRepository amenityRepository;

    @InjectMocks
    HotelServiceImpl hotelService;

    @Test
    void findHotels_shouldCallRepository() {
        //given
        List<Hotel> hotels = IntStream.range(1, 4).mapToObj(i -> new Hotel(i,
                        "Hotel name %d".formatted(i),
                        "Description %d".formatted(i),
                        "Brand %d".formatted(i),
                        new Address(),
                        new Contacts(),
                        new ArrivalTime(),
                        new ArrayList<>()))
                .collect(Collectors.toList());
        Mockito.doReturn(hotels).when(this.hotelRepository).findAll();

        //when
        List<Hotel> result = this.hotelService.findHotels();

        //then
        assertEquals(hotels, result);
        Mockito.verify(this.hotelRepository).findAll();
        Mockito.verifyNoMoreInteractions(this.hotelRepository);
    }

    @Test
    void findHotel_shouldCallRepository() {
        //given
        Hotel hotel = new Hotel(1,
                "name",
                "descriprtion",
                "brand",
                new Address(),
                new Contacts(),
                new ArrivalTime(),
                new ArrayList<>());
        Mockito.doReturn(Optional.of(hotel)).when(hotelRepository).findById(1);

        //when
        Hotel result = hotelService.findHotel(1);

        //then
        assertEquals(hotel, result);
        Mockito.verify(hotelRepository).findById(1);
        Mockito.verifyNoMoreInteractions(this.hotelRepository);
    }

    @Test
    void saveHotel_shouldCallRepository() {
        //given
        Hotel hotel = new Hotel(1,
                "name",
                "descriprtion",
                "brand",
                new Address(),
                new Contacts(),
                new ArrivalTime(),
                new ArrayList<>());
        Mockito.doReturn(hotel).when(hotelRepository).save(hotel);

        //when
        Hotel result = hotelService.saveHotel(hotel);

        //then
        assertEquals(hotel, result);
        Mockito.verify(hotelRepository).save(hotel);
        Mockito.verifyNoMoreInteractions(this.hotelRepository);
    }

    @Test
    void saveHotelAmenities_shouldUpdateAmenities_whenHotelExists() {
        // given
        Integer hotelId = 1;
        List<Amenity> amenities = Arrays.asList(
                new Amenity("Pool"),
                new Amenity("Gym"),
                new Amenity("Spa")
        );

        Hotel hotel = new Hotel(hotelId, "Hotel 1", "Description", "Brand", new Address(), new Contacts(), new ArrivalTime(), new ArrayList<>());
        Mockito.doReturn(Optional.of(hotel)).when(hotelRepository).findById(hotelId);

        Set<String> amenityNames = amenities.stream().map(Amenity::getName).collect(Collectors.toSet());
        List<Amenity> existingAmenities = Collections.singletonList(new Amenity("Pool"));
        Mockito.doReturn(existingAmenities).when(amenityRepository).findByNameIn(amenityNames);
        Mockito.doReturn(existingAmenities.get(0)).when(amenityRepository).save(ArgumentMatchers.any(Amenity.class));
        Mockito.doReturn(hotel).when(hotelRepository).save(ArgumentMatchers.any(Hotel.class));
        // when
        Hotel updatedHotel = hotelService.saveHotelAmenities(hotelId, amenities);

        // then
        assertNotNull(updatedHotel);
        assertEquals(3, updatedHotel.getAmenities().size());
        Mockito.verify(hotelRepository).findById(hotelId);
        Mockito.verify(amenityRepository).findByNameIn(amenityNames);
        Mockito.verify(amenityRepository, times(2)).save(ArgumentMatchers.any(Amenity.class));
        Mockito.verify(hotelRepository).save(hotel);
    }

    @Test
    void saveHotelAmenities_shouldThrowException_whenHotelNotFound() {
        // given
        Integer hotelId = 1;
        List<Amenity> amenities = Arrays.asList(
                new Amenity("Pool"),
                new Amenity("Gym"),
                new Amenity("Spa")
        );
        Mockito.when(hotelRepository.findById(hotelId)).thenReturn(Optional.empty());

        // when & then
        assertThrows(HotelNotFoundException.class, () -> hotelService.saveHotelAmenities(hotelId, amenities));
        Mockito.verify(hotelRepository).findById(hotelId);
        Mockito.verify(amenityRepository, Mockito.never()).findByNameIn(anySet());
        Mockito.verify(amenityRepository, Mockito.never()).save(ArgumentMatchers.any(Amenity.class));
        Mockito.verify(hotelRepository, Mockito.never()).save(ArgumentMatchers.any(Hotel.class));
    }

    @Test
    void saveHotelAmenities_shouldUseExistingAmenities_whenTheyAlreadyExist() {
        // given
        Integer hotelId = 1;
        List<Amenity> amenities = Arrays.asList(
                new Amenity("Pool"),
                new Amenity("Gym"),
                new Amenity("Spa")
        );

        Hotel hotel = new Hotel(hotelId, "Hotel 1", "Description", "Brand", new Address(), new Contacts(), new ArrivalTime(), new ArrayList<>());
        Mockito.when(hotelRepository.findById(hotelId)).thenReturn(Optional.of(hotel));
        Mockito.doReturn(hotel).when(hotelRepository).save(ArgumentMatchers.any(Hotel.class));

        Set<String> amenityNames = amenities.stream().map(Amenity::getName).collect(Collectors.toSet());
        List<Amenity> existingAmenities = Arrays.asList(
                new Amenity("Pool"),
                new Amenity("Gym")
        );
        Mockito.when(amenityRepository.findByNameIn(amenityNames)).thenReturn(existingAmenities);

        Mockito.when(amenityRepository.save(ArgumentMatchers.any(Amenity.class))).thenAnswer(invocation -> {
            Amenity amenity = invocation.getArgument(0);
            amenity.setId(new Random().nextInt());
            return amenity;
        });

        // when
        Hotel updatedHotel = hotelService.saveHotelAmenities(hotelId, amenities);

        // then
        assertNotNull(updatedHotel);
        assertEquals(3, updatedHotel.getAmenities().size());
        Mockito.verify(hotelRepository).findById(hotelId);
        Mockito.verify(amenityRepository).findByNameIn(amenityNames);
        Mockito.verify(amenityRepository, times(1)).save(ArgumentMatchers.any(Amenity.class));
        Mockito.verify(hotelRepository).save(hotel);
    }

    @Test
    void searchHotel_shouldReturnHotelList_whenSearchCriteriaFits() {
        //given
        List<Hotel> hotels = IntStream.range(0, 4).mapToObj(i -> new Hotel(i,
                        "Hotel name %d".formatted(i),
                        "Description %d".formatted(i),
                        "Brand %d".formatted(i),
                        new Address(i,
                                "street %d".formatted(i),
                                "city %d".formatted(i),
                                "country %d".formatted(i),
                                "postcode"),
                        new Contacts(),
                        new ArrivalTime(),
                        new ArrayList<>()))
                .toList();
        HotelSearchCriteria hotelSearchCriteria = new HotelSearchCriteria("1", null, null, null, null);
        HotelSpecification hotelSpecification = new HotelSpecification(hotelSearchCriteria);
        Hotel searchedHotel = hotels.get(1);
        ArrayList<Hotel> searchedHotels = new ArrayList<>();
        searchedHotels.add(searchedHotel);
        Mockito.doReturn(searchedHotels).when(hotelRepository).findAll(ArgumentMatchers.any(HotelSpecification.class));

        //when
        List<Hotel> result = hotelService.searchHotel(hotelSearchCriteria);

        //then
        assertEquals(searchedHotels, result);
        Mockito.verify(hotelRepository).findAll(hotelSpecification);
        Mockito.verifyNoMoreInteractions(hotelRepository);
    }

    @Test
    void searchHotel_shouldReturnEmptyList_whenSearchCriteriaNotFits() {
        //given
        List<Hotel> hotels = IntStream.range(0, 4).mapToObj(i -> new Hotel(i,
                        "Hotel name %d".formatted(i),
                        "Description %d".formatted(i),
                        "Brand %d".formatted(i),
                        new Address(i,
                                "street %d".formatted(i),
                                "city %d".formatted(i),
                                "country %d".formatted(i),
                                "postcode"),
                        new Contacts(),
                        new ArrivalTime(),
                        new ArrayList<>()))
                .toList();
        HotelSearchCriteria hotelSearchCriteria = new HotelSearchCriteria("5", null, null, null, null);
        HotelSpecification hotelSpecification = new HotelSpecification(hotelSearchCriteria);
        ArrayList<Hotel> searchedHotels = new ArrayList<>();
        Mockito.doReturn(searchedHotels).when(hotelRepository).findAll(ArgumentMatchers.any(HotelSpecification.class));

        //when
        List<Hotel> result = hotelService.searchHotel(hotelSearchCriteria);

        //then
        assertEquals(searchedHotels, result);
        Mockito.verify(hotelRepository).findAll(hotelSpecification);
        Mockito.verifyNoMoreInteractions(hotelRepository);
    }

    @Test
    void getHistogram_shouldReturnMap_whenValidArgument() {
        //given
        HashMap<String, Long> expectedResult = new HashMap<>();
        expectedResult.put("Minsk", 12L);
        expectedResult.put("Grodno", 5L);
        expectedResult.put("Baranovichy", 2L);
        String param = "city";
        HistogramResult minsk = new HistogramResult("Minsk", 12L);
        HistogramResult grodno = new HistogramResult("Grodno", 5L);
        HistogramResult baranovichy = new HistogramResult("Baranovichy", 2L);
        ArrayList<HistogramResult> histogramResults = new ArrayList<>();
        histogramResults.add(minsk);
        histogramResults.add(grodno);
        histogramResults.add(baranovichy);
        Mockito.doReturn(histogramResults).when(hotelRepository).countByAddressCity();
        //when

        Map<String, Long> result = hotelService.getHistogram(param);

        //then
        assertEquals(expectedResult, result);
        Mockito.verify(hotelRepository).countByAddressCity();
        Mockito.verifyNoMoreInteractions(hotelRepository);
    }
}