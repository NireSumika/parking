package cn.edu.dgut.parking.service;

import cn.edu.dgut.parking.model.ParkingLot;
import cn.edu.dgut.parking.model.Response;
import cn.edu.dgut.parking.repository.ParkingLotRepository;
import cn.edu.dgut.parking.util.Global;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ParkingLotService {
    @Autowired
    private ParkingLotRepository parkingLotRepository;
    @Autowired
    private Global global;

    public Response<?> add(ParkingLot parkingLot){
        if (null == parkingLotRepository.findByParkingLotName(parkingLot.getParkingLotName())){
            return Response.withData(parkingLotRepository.save(parkingLot));
        }
        return Response.failuer("已存在", 6001);
    }

    public Response<?> getOne(ParkingLot parkingLot){
        return Response.withData(parkingLotRepository.findByParkingLotName(parkingLot.getParkingLotName()));
    }

    public Response<?> getAll(){
        return Response.withData(parkingLotRepository.findAll());
    }

    public Response<?> update(ParkingLot parkingLot){
        ParkingLot original = parkingLotRepository.getOne(parkingLot.getId());
        if (null != original){
            if (null != original.getParkingLotName()) {
                original.setParkingLotName(parkingLot.getParkingLotName());
            }
            if (null != parkingLot.getRemaining()){
                original.setRemaining(parkingLot.getRemaining());
            }
            if (null != parkingLot.getMode()){
                original.setMode(parkingLot.getMode());
            }
            return Response.withData(parkingLotRepository.save(original));
        }
        return Response.failuer("不存在", 6001);
    }

//    public Response<?> update_mode(Integer mode){
//        try {
//            global.mode = mode;
//            List<ParkingLot> lots = parkingLotRepository.findAll();
//            if (lots.isEmpty()) return Response.failuer("未找到数据", 7004);
//            for (ParkingLot lot : lots){
//                lot.setMode(mode);
//            }
////            parkingLotRepository.saveAll(lots);
//            return Response.withData(lots);
//        }catch (Exception e){
//            return Response.failuer("error", 7001);
//        }
//    }

    public Response<?> del(ParkingLot parkingLot){
        ParkingLot original = parkingLotRepository.findByParkingLotName(parkingLot.getParkingLotName());
        if (null != original){
            parkingLotRepository.delete(original);
            return Response.success();
        }
        return Response.failuer("不存在", 6001);
    }
}
