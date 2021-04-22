package cn.edu.dgut.parking.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Global {
    @Value("${overOutTime}")
    public long outOverTime;
    @Value("${freeTime}")
    public long freeTime;
    @Value("${normalTimeFeeUnit}")
    public float normalTimeFeeUnit;
    @Value("${overTimeFeeUnit}")
    public float overTimeFeeUnit;
    @Value("${normalTimeFeeCount}")
    public float normalTimeFeeCount;
    @Value("${timeReduce}")
    public float timeReduce;
    @Value("${overTimeFeeCount}")
    public float overTimeFeeCount;
    @Value("${mode}")
    public Integer mode;

}
