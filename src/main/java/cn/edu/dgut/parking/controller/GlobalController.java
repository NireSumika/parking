package cn.edu.dgut.parking.controller;

import cn.edu.dgut.parking.model.GlobalModel;
import cn.edu.dgut.parking.model.Response;
import cn.edu.dgut.parking.util.Global;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RequestMapping("/global")
@RestController
public class GlobalController {
    @Autowired
    private Global global;

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
