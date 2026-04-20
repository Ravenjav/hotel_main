package by.ray.hotel_main.controller;

import by.ray.hotel_main.service.HotelService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Tag(name = "Histogram", description = "The Histogram API for hotels")
@RestController
@RequestMapping("/histogram")
@RequiredArgsConstructor
public class HistogramController {
    private final HotelService hotelService;
    @Operation(summary = "Get histogram by parameter",
            description = "Retrieve histogram data based on the provided parameter.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Histogram data successfully retrieved"),
            @ApiResponse(responseCode = "404", description = "Histogram data not found")
    })
    @GetMapping("/{param}")
    Map<String, Long> getHistogram(@PathVariable String param) {
        return hotelService.getHistogram(param);
    }
}
