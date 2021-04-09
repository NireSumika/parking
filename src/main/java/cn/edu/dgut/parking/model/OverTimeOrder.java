package cn.edu.dgut.parking.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OverTimeOrder implements Serializable {
    private Integer overTimeTime = 0;//超时时长
    private Integer overTimeFee = 0;//超时费用
    private LocalDateTime overTimeOrderSubmitTime;//超时订单提交时间
    private Boolean overTimeOutPaid = false;//是否已支付超时费用

}
