package by.ray.hotel_main.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.ValueInstantiationException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class HotelSearchCriteriaTest {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }
    @Test
    void shouldDeserializeWhenOnlyNameProvided() throws Exception {
        String json = """
                {
                  "name": "Radisson Blu"
                }""";

        HotelSearchCriteria criteria = objectMapper.readValue(json, HotelSearchCriteria.class);

        assertNotNull(criteria);
        assertEquals("Radisson Blu", criteria.getName());
        assertNull(criteria.getBrand());
        assertNull(criteria.getCity());
        assertNull(criteria.getCountry());
        assertNull(criteria.getAmenities());
    }

    @Test
    void shouldDeserializeWhenNameAndCityProvided() throws Exception {
        String json = """
                {
                  "name": "Radisson Blu",
                  "city": "Minsk"
                }""";

        HotelSearchCriteria criteria = objectMapper.readValue(json, HotelSearchCriteria.class);

        assertNotNull(criteria);
        assertEquals("Radisson Blu", criteria.getName());
        assertEquals("Minsk", criteria.getCity());
        assertNull(criteria.getBrand());
        assertNull(criteria.getCountry());
        assertNull(criteria.getAmenities());
    }

    @Test
    void shouldDeserializeWhenAllFieldsProvided() throws Exception {
        String json = """
                {
                  "name": "Radisson Blu",
                  "brand": "Radisson",
                  "city": "Minsk",
                  "country": "Belarus",
                  "amenities": ["Spa", "Pool"]
                }""";

        HotelSearchCriteria criteria = objectMapper.readValue(json, HotelSearchCriteria.class);

        assertNotNull(criteria);
        assertEquals("Radisson Blu", criteria.getName());
        assertEquals("Radisson", criteria.getBrand());
        assertEquals("Minsk", criteria.getCity());
        assertEquals("Belarus", criteria.getCountry());
        assertNotNull(criteria.getAmenities());
        assertEquals(2, criteria.getAmenities().size());
        assertTrue(criteria.getAmenities().contains("Spa"));
        assertTrue(criteria.getAmenities().contains("Pool"));
    }

    @Test
    void shouldFailValidationWhenJsonIsEmpty() throws JsonProcessingException {
        String json = "{}";

        HotelSearchCriteria criteria = objectMapper.readValue(json, HotelSearchCriteria.class);
        Set<ConstraintViolation<HotelSearchCriteria>> violations = validator.validate(criteria);

        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());

        ConstraintViolation<HotelSearchCriteria> violation = violations.iterator().next();
        assertEquals("At least one search criteria must be provided", violation.getMessage());
    }

    @Test
    void shouldFailValidationWhenAllFieldsAreNull() throws JsonProcessingException {
        String json = """
                {
                  "name": null,
                  "brand": null,
                  "city": null,
                  "country": null,
                  "amenities": null
                }""";

        HotelSearchCriteria criteria = objectMapper.readValue(json, HotelSearchCriteria.class);
        Set<ConstraintViolation<HotelSearchCriteria>> violations = validator.validate(criteria);

        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());

        ConstraintViolation<HotelSearchCriteria> violation = violations.iterator().next();
        assertEquals("At least one search criteria must be provided", violation.getMessage());
    }
}