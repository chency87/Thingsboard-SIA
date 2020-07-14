package org.thingsboard.server.controller.idscontroller;


import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.thingsboard.server.bean.DisplayCard;
import org.thingsboard.server.service.ids.AcideventService;
import org.thingsboard.server.service.ids.DashboardService;

@RestController
@Api(value = "Dashboard界面")
public class DashboardsController {
	
	@Autowired
	private DashboardService dashBoard;
	
	@Autowired
	private AcideventService iacids;
	
	@RequestMapping(value = "/dashboard/displayCardInfo",method= RequestMethod.GET)
	@ApiOperation(value = "获取DashBoard上方展示卡信息", notes = "查询对应的值")
	public DisplayCard displayCardInfo(){
		DisplayCard dc = new DisplayCard();
		dc.setNumOfAbnormal(iacids.queryAbnormaltraffic());
		dc.setNumOfDataCache(iacids.cacheData());
		dc.setNumOfDevice(iacids.querydevicecount());
		dc.setNumOfProtocol(iacids.proCount());
		return dc;
	}
	
	/**
	 * 	获取DashBoard上方网络流量趋势信息
	 * @param start		
	 * @param end
	 * @return Map<x(y)轴，List<值>>
	 */
	@RequestMapping(value = "/dashboard/networktraffictend",method= RequestMethod.GET)
	@ApiOperation(value = "", notes = "以HashMap形式返回对应x,y值查询对应的值，x为时间，y为流量值，默认计算当前时间前的7个小时")
//	@ApiImplicitParams({@ApiImplicitParam(paramType="String", name = "start", value = "流量趋势计算开始时间", required = true, dataType = "String"), @ApiImplicitParam(paramType="String", name = "end", value = "流量趋势计算结束时间", required = true, dataType = "String")})
	public Map<String,List<String>> networkTrafficTend(
			@RequestParam(name = "start") String start,
			@RequestParam(name = "end") String end){
		
		return dashBoard.networkTrafficTend(start, end);
	}
	
	/**
	 * 获取DashBoard上方异常行为周报信息
	 * @return
	 */
	@RequestMapping(value = "/dashboard/networkabnormalcountinfo",method= RequestMethod.GET)
	@ApiOperation(value = "获取DashBoard上方异常行为周报信息", notes = "以HashMap形式返回对应x,y值查询对应的值，x为时间，y为异常数量")
	public Map<String,List<String>> networkAbnormalCountInfo(){
		
		return dashBoard.networkAbnormalCountInfo();
	}
	

		
}
