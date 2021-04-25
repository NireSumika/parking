package cn.edu.dgut.parking.controller;

import cn.edu.dgut.parking.model.GlobalModel;
import cn.edu.dgut.parking.model.ParkingLot;
import cn.edu.dgut.parking.model.Response;
import cn.edu.dgut.parking.service.ParkingLotService;
import cn.edu.dgut.parking.util.Global;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RequestMapping("/parkingLot")
@RestController
public class ParkingLotController {
    @Autowired
    private ParkingLotService parkingLotService;
    @Autowired
    private Global global;

    @PostMapping("/add")
    public Response<?> add(HttpServletRequest request, @RequestBody ParkingLot parkingLot){
        String uid = request.getAttribute("claims").toString();
        if (!uid.equals("root")) return Response.failuer("无权限", 4003);
        if (parkingLot.getRemaining() == null || parkingLot.getParkingLotName() == null){
            return Response.failuer("存在空数据", 6002);
        }
        return parkingLotService.add(parkingLot);
    }

    @PostMapping("/getOne")
    public Response<?> getOne(@RequestBody ParkingLot parkingLot){
        return parkingLotService.getOne(parkingLot);
    }

    @GetMapping("/getAll")
    public Response<?> getAll(){
        return parkingLotService.getAll();
    }

    @PostMapping("/update")
    public Response<?> update(HttpServletRequest request, @RequestBody ParkingLot parkingLot){
        String uid = request.getAttribute("claims").toString();
        if (!uid.equals("root")) return Response.failuer("无权限", 4003);
        return parkingLotService.update(parkingLot);
    }
//    @GetMapping("/update_mode")
//    public Response<?> update_mode(HttpServletRequest request, @RequestParam Integer mode){
//        String uid = request.getAttribute("claims").toString();
//        if (!uid.equals("root")) return Response.failuer("无权限", 4003);
//        return parkingLotService.update_mode(mode);
//    }

    @PostMapping("/del")
    public Response<?> del(HttpServletRequest request, @RequestBody ParkingLot parkingLot){
        String uid = request.getAttribute("claims").toString();
        if (!uid.equals("root")) return Response.failuer("无权限", 4003);
        return parkingLotService.del(parkingLot);
    }

    @GetMapping("/get_global")
    public Response<?> get_global(){
        return Response.withData(global);
    }

    @PostMapping("/update_global")
    public Response<?> update_global(HttpServletRequest request, @RequestBody GlobalModel globalModel){
        global.outOverTime = globalModel.getOutOverTime();
        global.freeTime = globalModel.getFreeTime();
        global.normalTimeFeeUnit = globalModel.getNormalTimeFeeUnit();
        global.overTimeFeeUnit = globalModel.getOverTimeFeeUnit();
        global.normalTimeFeeCount = globalModel.getNormalTimeFeeCount();
        global.overTimeFeeCount = globalModel.getOverTimeFeeCount();
        global.timeReduce = globalModel.getTimeReduce();
        global.mode = globalModel.getMode();
        return Response.withData(global);
    }
}
