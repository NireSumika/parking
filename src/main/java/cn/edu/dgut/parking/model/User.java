package cn.edu.dgut.parking.model;

import lombok.*;

import javax.persistence.*;

@Data
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Entity
@Table(name = "UserData")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String uid;
    @Column(unique = true, length = 15)
    private String phoneNum;
    @Column
    private String nickName;
    @Column
    private String gender;
    @Column
    private String city;
    @Column
    private String avatarUrl;
    @Column
    private String code;
    @Column
    private String session_key;
    @Column
    private String token;
}
