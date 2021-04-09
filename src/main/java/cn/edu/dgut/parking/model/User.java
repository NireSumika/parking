package cn.edu.dgut.parking.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

@Data
@ToString(exclude = {"cars", "orders"})
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Entity
@Table(name = "UserData")
@JsonIgnoreProperties(value = {"cars", "orders"})
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
    private Integer member = 0;//会员
    @Column
    private LocalDateTime memberTime;//会员到期日期
    @Column
    private String token;
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    @NotFound(action = NotFoundAction.IGNORE)
    private Set<Order> orders;
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private Set<Car> cars;

    public User copyUser(){
        User user = new User();
        user.setId(getId());
        user.setUid(getUid());
        user.setPhoneNum(getPhoneNum());
        user.setNickName(getNickName());
        user.setGender(getGender());
        user.setCity(getCity());
        user.setAvatarUrl(getAvatarUrl());
        user.setCode(getCode());
        user.setSession_key(getSession_key());
        user.setToken(getToken());
        user.setMember(getMember());
        user.setMemberTime(getMemberTime());
        return user;
    }
}
