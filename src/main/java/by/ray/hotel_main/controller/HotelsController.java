package by.ray.hotel_main.controller;

import by.ray.hotel_main.dto.HotelDetailedDTO;
import by.ray.hotel_main.dto.HotelPostDTO;
import by.ray.hotel_main.dto.HotelSummaryDTO;
import by.ray.hotel_main.mapper.HotelMapper;
import by.ray.hotel_main.model.Amenity;
import by.ray.hotel_main.model.Hotel;
import by.ray.hotel_main.service.HotelService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "Hotel", description = "The Hotel API")
@RestController
@RequestMapping("/hotels")
@RequiredArgsConstructor
@Validated
public class HotelsController {
    private final HotelService hotelService;
    private final HotelMapper hotelMapper;

    @Operation(summary = "Get a list of hotels")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the hotels",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = HotelSummaryDTO.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid request parameters",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Hotels not found",
                    content = @Content)})
    @GetMapping
    List<HotelSummaryDTO> findHotels() {
        List<Hotel> hotels = hotelService.findHotels();
        return hotels.stream().map(hotelMapper::mapToSummaryDTO).toList();
    }

    @Operation(summary = "Get a hotel by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the hotel",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = HotelDetailedDTO.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid ID supplied",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Hotel not found",
                    content = @Content)})
    @GetMapping("/{id}")
    HotelDetailedDTO findHotel(@PathVariable Integer id) {
        return hotelMapper.mapToDetailedDTO(hotelService.findHotel(id));
    }

    @Operation(summary = "Save a new hotel")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Hotel created",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = HotelSummaryDTO.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid input",
                    content = @Content)})
    @PostMapping
    HotelSummaryDTO saveHotel(@Valid @RequestBody HotelPostDTO hotelPostDTO) {
        Hotel hotelToSave = hotelMapper.mapToDetailedDTO(hotelPostDTO);
        Hotel savedHotel = hotelService.saveHotel(hotelToSave);
        return hotelMapper.mapToSummaryDTO(savedHotel);
    }
    @Operation(summary = "Save amenities for a hotel")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Amenities saved",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = HotelSummaryDTO.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid input",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Hotel not found",
                    content = @Content)})
    @PostMapping("/{id}/amenities")
    HotelSummaryDTO saveHotelAmenities(@PathVariable Integer id, @RequestBody List<@NotEmpty String> amenities) {
        List<Amenity> amenityList = amenities.stream().map(Amenity::new).collect(Collectors.toList());
        return hotelMapper.mapToSummaryDTO(hotelService.saveHotelAmenities(id, amenityList));
    }
}
