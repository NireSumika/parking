package cn.edu.dgut.parking.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import java.util.Set;

@Data
@ToString(exclude = {"orders", "user"})
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@Entity
@JsonIgnoreProperties(value = {"user"})
public class Car {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true)
    private String lincesePlate;
    @OneToMany(mappedBy = "car", fetch = FetchType.LAZY)
    private Set<Order> orders;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public Car copyCar(){
        Car car = new Car();
        car.setId(getId());
        car.setLincesePlate(getLincesePlate());
        return car;
    }
}
