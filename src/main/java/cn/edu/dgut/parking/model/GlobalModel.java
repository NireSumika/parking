package cn.edu.dgut.parking.model;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class GlobalModel {
    private long outOverTime;
    private long freeTime;
    private float normalTimeFeeUnit;
    private float overTimeFeeUnit;
    private float normalTimeFeeCount;
    private float timeReduce;
    private float overTimeFeeCount;
    private Integer mode;
}
