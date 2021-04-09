package cn.edu.dgut.parking.service;

import cn.edu.dgut.parking.model.Order;
import cn.edu.dgut.parking.model.OverTimeOrder;
import cn.edu.dgut.parking.util.Global;
import com.fasterxml.jackson.datatype.jsr310.deser.DurationDeserializer;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import java.time.Duration;
import java.time.LocalDateTime;

@Component
public class CalculateFee {
    @Autowired
    private Global global;

    public Integer CalculateTempFee(LocalDateTime inTime){
        Duration duration = Duration.between(inTime, LocalDateTime.now());
        long time = duration.toMinutes();
        time = time - global.freeTime;
        if (time <= 0) return 0;
        float reduceTime = (time % (global.normalTimeFeeCount * 60));
        if (reduceTime > global.timeReduce){
            return (int)(((int)(time / (global.normalTimeFeeCount * 60)) + 1) * global.normalTimeFeeUnit * 100);
        }

        return (int)((int)(time / (global.normalTimeFeeCount * 60)) * global.normalTimeFeeUnit * 100);
    }

    public Integer CalculateNormalFee(Order order){
        Duration duration = Duration.between(order.getInTime(), order.getOrderSubmissionTime());
        long time = duration.toMinutes();
        time = time - global.freeTime;
        if (time <= 0) return 0;
        float reduceTime = (time % (global.normalTimeFeeCount * 60));
        if (reduceTime > global.timeReduce){
            return (int)(((int)(time / (global.normalTimeFeeCount * 60)) + 1) * global.normalTimeFeeUnit * 100);
        }
        return (int)((int)(time / (global.normalTimeFeeCount * 60)) * global.normalTimeFeeUnit * 100);
    }

    public Integer CalculateOverTimeFee(OverTimeOrder order){
        int time = order.getOverTimeTime();
        float reduceTime = (time % (global.overTimeFeeCount * 60));
        if (reduceTime > global.timeReduce){
            return (int)(((int)(time / (global.overTimeFeeCount * 60)) + 1) * global.overTimeFeeUnit * 100);
        }
        return (int)((int)(time / (global.overTimeFeeCount * 60)) * global.overTimeFeeUnit * 100);
    }
}