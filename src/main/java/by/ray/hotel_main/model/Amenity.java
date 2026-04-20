package by.ray.hotel_main.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "amenity")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Amenity {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "name")
    private String name;

    public Amenity(String s) {
        this(null, s);
    }
}
