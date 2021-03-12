package campuslifecenter.common.model;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@Entity
@Table
public class Role implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String name;

    @Column(updatable = false)
    @CreatedDate
    private LocalDateTime createTime;

    @ManyToOne
    private User belong;

    private boolean pub;

    @ManyToMany(mappedBy = "roles")
    private Set<User> users;

}
