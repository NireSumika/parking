package cn.edu.dgut.parking.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Builder
@ToString(exclude = {"user", "car"})
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"id"})
@Entity
@Table(name = "OrderData")
@JsonIgnoreProperties(value = {"user", "car"})
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;//订单id
    @Column(nullable = false, unique = true)
    private String orderNum;//订单号
    @Column(nullable = false)
    private Boolean orderCompleted = false;//订单是否已完成
    @Column(nullable = false)
    private String parkingLotName;//停车场名称
    @Column(nullable = false)
    private String licensePlate;//车牌号
    @Column(nullable = false)
    private LocalDateTime inTime;//入场时间
    @Column(nullable = false)
    private String inGate;//入场口
    @Column(nullable = false)
    private String inPassWay;//入场放行方式
    @Column
    private String inPicturePath;//入场抓拍照片路径
    @Column
    private LocalDateTime outTime;//出场时间
    @Column
    private String outGate;//出场口
    @Column
    private String outPassWay;//出场放行方式
    @Column()
    private String outPicturePath;//出场抓拍照片路径
    @Column
    private LocalDateTime orderSubmissionTime;//订单提交时间
    @Column
    private Long parkingTime;//停车时长（秒）
    @Column
    private Integer parkingFee = 0;//停车费用
    @Column
    private Integer actualFee = 0;//实际费用，考虑会员等
    @Column(nullable = false)
    private Boolean paid = false;//是否支付
    @Column
    private LocalDateTime payTime;//支付时间
    @Column(nullable = false)
    private Integer overTimeOut = 0;//是否超时离场
    @Column(length = 2048)
    private String overTimeInfo;//超时记录
    @Column
    private Boolean releaseFlag = false;//是否放行
    @Column(nullable = false)
    private Boolean orderExist = true;//删除flag
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "car_id")
    private Car car;

    public Order copyOrder(){
        Order order = new Order();
        order.setId(getId());
        return order;
    }
}
