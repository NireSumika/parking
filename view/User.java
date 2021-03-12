package campuslifecenter.common.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
@JsonIgnoreProperties(value = {"hibernateLazyInitializer"})
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private Long signId;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    @Basic(fetch = FetchType.LAZY)
    @JsonIgnore
    private String password;
    @Enumerated
    private Gender gender;

    @Column(updatable = false)
    @CreatedDate
    private LocalDateTime createTime;

    @ManyToMany
    @JoinTable(
            name = "user_roles",
            joinColumns = {
                    @JoinColumn(name = "uid", referencedColumnName = "id")
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "rid", referencedColumnName = "id")
            }
    )
    private Set<Role> roles;

    @OneToMany(mappedBy = "belong")
    private Set<Role> createRole;

}
