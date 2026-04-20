package by.ray.hotel_main.specification;


import by.ray.hotel_main.dto.HotelSearchCriteria;
import by.ray.hotel_main.model.Amenity;
import by.ray.hotel_main.model.Hotel;
import jakarta.persistence.criteria.*;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@EqualsAndHashCode
public class HotelSpecification implements Specification<Hotel> {
    private final HotelSearchCriteria criteria;

    @Override
    public Predicate toPredicate(Root<Hotel> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        List<Predicate> predicates = new ArrayList<>();

        String name = criteria.getName();
        String brand = criteria.getBrand();
        String city = criteria.getCity();
        String country = criteria.getCountry();
        List<String> amenities = criteria.getAmenities();

        if(name != null && !name.isBlank()) {
            predicates.add(builder.like(builder.lower(root.get("name")), "%" + name.toLowerCase() + "%"));
        }
        if(brand != null && !brand.isBlank()) {
            predicates.add(builder.like(builder.lower(root.get("brand")), "%" + brand.toLowerCase() + "%"));
        }
        if(city != null && !city.isBlank()) {
            predicates.add(builder.like(builder.lower(root.get("address").get("city")), "%" + city.toLowerCase() + "%"));
        }
        if(country != null && !country.isBlank()) {
            predicates.add(builder.like(builder.lower(root.get("address").get("country")), "%" + country.toLowerCase() + "%"));
        }
        if (amenities != null && !amenities.isEmpty()){
            Join<Hotel, Amenity> rootAmenities = root.join("amenities", JoinType.INNER);
            List<Predicate> amenityPredicates = new ArrayList<>();
            for (String amenity : amenities){
                amenityPredicates.add(builder.like(builder.lower(rootAmenities.get("name")), "%" + amenity.toLowerCase() + "%"));
            }
            Predicate or = builder.or(amenityPredicates.toArray(new Predicate[0]));
            predicates.add(or);
        }
        return builder.and(predicates.toArray(new Predicate[0]));
    }
}
