package cn.edu.dgut.parking.service;


import cn.edu.dgut.parking.model.Order;
import cn.edu.dgut.parking.model.OverTimeOrder;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
public class OverTimeHandler {

    @Autowired
    private CalculateFee calculateFee;
    @Autowired
    private ObjectMapper objectMapper;

    public OverTimeOrder creatOverTimeOrder(Order order, int outFlag) throws JsonProcessingException {
        OverTimeOrder overTimeOrder = new OverTimeOrder();
        Duration calculateOverTime = Duration.between(order.getOrderSubmissionTime(), LocalDateTime.now());
        if (outFlag > 2) {
            calculateOverTime = Duration.between(transToObject(order.getOverTimeInfo()).getOverTimeOrderSubmitTime(), LocalDateTime.now());
        }
        overTimeOrder.setOverTimeTime((int)calculateOverTime.toMinutes());
        overTimeOrder.setOverTimeFee(calculateFee.CalculateOverTimeFee(overTimeOrder));
        return overTimeOrder;
    }
    public String transToString(OverTimeOrder overTimeOrder, boolean subTime, boolean paid) throws JsonProcessingException {
//        ObjectMapper objectMapper = new ObjectMapper();
        if (subTime){
            overTimeOrder.setOverTimeOrderSubmitTime(LocalDateTime.now());
        }
        if (paid){
            overTimeOrder.setOverTimeOutPaid(true);
        }
        return objectMapper.writeValueAsString(overTimeOrder);
    }

    public OverTimeOrder transToObject(String info) throws JsonProcessingException {
//        ObjectMapper objectMapper = new ObjectMapper();
        //序列化的时候序列对象的所有属性
        objectMapper.setSerializationInclusion(JsonInclude.Include.ALWAYS);

        //反序列化的时候如果多了其他属性,不抛出异常
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        //如果是空对象的时候,不抛异常
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        OverTimeOrder overTimeOrder = objectMapper.readValue(info, OverTimeOrder.class);
        if (null != overTimeOrder){
            return overTimeOrder;
        }
        return null;
    }
    public static void main(String args[]) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        //序列化的时候序列对象的所有属性
        objectMapper.setSerializationInclusion(JsonInclude.Include.ALWAYS);

        //反序列化的时候如果多了其他属性,不抛出异常
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        //如果是空对象的时候,不抛异常
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
//        OverTimeOrder overTimeOrder= new OverTimeOrder(null, 11L, 11, null,null);
//        OverTimeOrder overTimeOrder1= new OverTimeOrder(null, 22L, 22, null,true);
//        overTimeOrder.setOverTimeFee(1);
        List list = new ArrayList<>();
//        list.add(overTimeOrder);
//        list.add(overTimeOrder1);
        String string = objectMapper.writeValueAsString(list);
        System.out.println(string);
    }
}
