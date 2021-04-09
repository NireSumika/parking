package cn.edu.dgut.parking.model;

import lombok.*;

import javax.persistence.*;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"adminId"})
@Entity
@Table(name = "AdminUserData")
public class AdminUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true, updatable = false)
    private String adminId;
    @Column
    private String nickName;
    @Column(nullable = false)
    private String passWd;
    @Column
    private String token;
}
