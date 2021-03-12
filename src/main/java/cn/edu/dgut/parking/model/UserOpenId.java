package cn.edu.dgut.parking.model;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class UserOpenId {
    private String session_key;
    private String openid;
}
