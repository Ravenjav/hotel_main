package by.ray.hotel_main.exception;

public class HotelNotFoundException extends RuntimeException {
    public HotelNotFoundException(Integer id) {
        super(String.format("Hotel with id %d not found", id));
    }
}
