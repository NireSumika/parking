package cn.edu.dgut.parking.model;

import lombok.*;
import org.hibernate.annotations.Columns;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Entity
@Table(name = "tesst")

public class Test {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    @Column(updatable = false)
    private LocalDateTime inTime;
    @Column(updatable = false)
    private LocalDateTime outTime;
    @Column(updatable = false)
    private Long totalTime;
}
