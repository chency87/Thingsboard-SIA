package org.thingsboard.server.controller.idscontroller;


import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.thingsboard.server.service.ids.TrafficTrendService;

@RestController
@RequestMapping("/api")
public class TrafficTrendController {
	
	@Autowired
	private TrafficTrendService trafficTrendService;
//
//	@Autowired
//	private AcideventService iacids;
	
//	@RequestMapping(value = "/dashboard/displayCardInfo",method= RequestMethod.GET)
//	@ApiOperation(value = "获取DashBoard上方展示卡信息", notes = "查询对应的值")
//	public DisplayCard displayCardInfo(){
//		DisplayCard dc = new DisplayCard();
//		dc.setNumOfAbnormal(iacids.queryAbnormaltraffic());
//		dc.setNumOfDataCache(iacids.cacheData());
//		dc.setNumOfDevice(iacids.querydevicecount());
//		dc.setNumOfProtocol(iacids.proCount());
//		return dc;
//	}
	
	/**
	 * 	获取DashBoard上方网络流量趋势信息
	 * @param start		
	 * @param end
	 * @return Map<x(y)轴，List<值>>
	 */
	@PreAuthorize("hasAnyAuthority('SYS_ADMIN', 'TENANT_ADMIN', 'CUSTOMER_USER')")
	@RequestMapping(value = "/trafficTrend/networktraffictend",method= RequestMethod.GET)
//	@ApiOperation(value = "", notes = "以HashMap形式返回对应x,y值查询对应的值，x为时间，y为流量值，默认计算当前时间前的7个小时")
//	@ApiImplicitParams({@ApiImplicitParam(paramType="String", name = "start", value = "流量趋势计算开始时间", required = true, dataType = "String"), @ApiImplicitParam(paramType="String", name = "end", value = "流量趋势计算结束时间", required = true, dataType = "String")})
	public Map<String,List<String>> networkTrafficTend(
			@RequestParam(name = "start") String start,
			@RequestParam(name = "end") String end){
		
		return trafficTrendService.networkTrafficTend(start, end);
	}
	
	/**
	 * 获取DashBoard上方异常行为周报信息
	 * @return
	 */
	@PreAuthorize("hasAnyAuthority('SYS_ADMIN', 'TENANT_ADMIN', 'CUSTOMER_USER')")
	@RequestMapping(value = "/trafficTrend/networkabnormalcountinfo",method= RequestMethod.GET)
//	@ApiOperation(value = "获取DashBoard上方异常行为周报信息", notes = "以HashMap形式返回对应x,y值查询对应的值，x为时间，y为异常数量")
	public Map<String,List<String>> networkAbnormalCountInfo(){
		
		return trafficTrendService.networkAbnormalCountInfo();
	}
	

		
}
