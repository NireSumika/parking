package cn.edu.dgut.parking.model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Entity
@Table(name = "OrderData")

public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;//订单id
    @Column(nullable = false)
    private String orderNum;
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
    @Column
    private String outPicturePath;//出场抓拍照片路径
    @Column
    private LocalDateTime orderSubmissionTime;//订单提交时间
    @Column
    private Long parkingTime;//停车时长（秒）
    @Column
    private Integer parkingFee;//停车费用
    @Column(nullable = false)
    private Boolean paid = false;//是否支付
    @Column
    private LocalDateTime payTime;//支付时间
    @Column(nullable = false)
    private Boolean overTimeOut = false;//是否超时离场
    @Column
    private Long overTimeTime;//超时时长
    @Column
    private Integer overTimeFee;//超时费用
    @Column(nullable = false)
    private Boolean orderExist = true;//删除flag
}
