package by.ray.hotel_main.repository;

import by.ray.hotel_main.model.Amenity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Set;

public interface AmenityRepository extends JpaRepository<Amenity, Integer> {
    List<Amenity> findByNameIn(Set<String> amenityNames);
}
