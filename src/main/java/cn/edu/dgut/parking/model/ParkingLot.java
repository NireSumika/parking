package cn.edu.dgut.parking.model;

import lombok.*;

import javax.persistence.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@Entity
public class ParkingLot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, nullable = false)
    private String parkingLotName;
    @Column(nullable = false)
    private Integer remaining; //剩余车位
    @Column(nullable = false)
    private Integer mode = 1;//工作模式
}
