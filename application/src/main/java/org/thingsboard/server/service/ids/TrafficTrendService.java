package org.thingsboard.server.service.ids;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.log4j.Log4j2;

import org.thingsboard.server.dao.sql.secgate.TrafficTrendRepository;
import org.thingsboard.server.dao.sql.secgate.EventsRepository;
import org.thingsboard.server.dao.sql.secgate.IphdrRepository;
import org.thingsboard.server.utils.CalculationIntervalUtils;

@Log4j2
@Service
public class TrafficTrendService {
	
	@Autowired
	private TrafficTrendRepository trafficTrendDao;
	@Autowired
	private EventsRepository eventDao;
	
	@Autowired
	private IphdrRepository iphdrDao;

	public List<Integer> displayCard() {
		// TODO Auto-generated method stub
		
		return null;
	}


	public Map<String, List<String>> networkTrafficTend(String start, String end) {
		
		String intervalStart = null;
		String intervalEnd = null;
		long unitConversion = 8*1024;
		
		Map<String,List<String>> ret = new HashMap<String,List<String>>();
		List<String> xAxis = new ArrayList<String>();
		List<String> data = new ArrayList<String>();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		ArrayList<Date> intervalList = CalculationIntervalUtils.getInterval(start, end, 10);
		for(Date date : intervalList) {
			String time = dateFormat.format(date);
			xAxis.add(time);
		}
		
		for(int i=0; i<xAxis.size() - 1; i++) {
			intervalStart = xAxis.get(i); 
			intervalEnd = xAxis.get(i+1); 
			
			Long dataSum = trafficTrendDao.getIntervalDataSum(intervalStart, intervalEnd);
			if (dataSum!=null){
				Double dataMb = dataSum/(double)unitConversion;
				data.add(dataMb.toString());
			}
			else {
				continue;
			}

		}
		Long dataSum = trafficTrendDao.getIntervalDataSum(intervalStart, intervalEnd);
//		cchong
		if (dataSum!=null){
			Double dataMb = dataSum/(double)unitConversion;
			data.add(dataMb.toString());
		}
		else {
			Double dataMb = (double)0;
			data.add(dataMb.toString());
		}
//		data.add(String.valueOf(dataMb));
		ret.put("xAxis", xAxis);
		ret.put("data",data);
		return ret;
		
	}


	public Map<String, List<String>> networkAbnormalCountInfo() {

		String intervalStart = null;
		String intervalEnd = null;
		int nDaysAgo = 15;
		long nDaysSecond = nDaysAgo*24*60*60*1000;
		Map<String,List<String>> ret = new HashMap<String,List<String>>();
		List<String> xAxis = new ArrayList<String>();
		List<String> data = new ArrayList<String>();
		Date nDaysAgoDate = new Date();
		Date nowDate = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String end = dateFormat.format(nowDate);
		long endSecond = nowDate.getTime();
		long startSecond = endSecond - nDaysSecond;
		nDaysAgoDate.setTime(startSecond);
		String start = dateFormat.format(nDaysAgoDate);
		System.out.println("start..."+ start + "  end..." + end);
		ArrayList<Date> intervalList = CalculationIntervalUtils.getInterval(start, end, nDaysAgo);
		for(Date date : intervalList) {
			String time = dateFormat.format(date);
			xAxis.add(time);
		}

		for(int i=0; i<xAxis.size()-1; i++) {
			intervalStart = xAxis.get(i); 
			intervalEnd = xAxis.get(i+1);
//			System.out.println("----------------");
//			System.out.println(intervalStart);
//			System.out.println(intervalEnd);
//			System.out.println(eventDao.getCountsBytime(intervalStart, intervalEnd));
			Integer countsBytime = eventDao.getCountsBytime(intervalStart, intervalEnd);

			data.add(countsBytime.toString());
		}
		Integer countsBytime = eventDao.getCountsBytime(intervalStart, intervalEnd);
		data.add(countsBytime.toString());
		
		ret.put("xAxis", xAxis);
		ret.put("data",data);
		return ret;
	}



	public Map<String, String> networkAlertDevInfo() {
		
		Map<String,String> ret = new HashMap<String,String>();
		int nDaysAgo = 15;
		long nDaysSecond = nDaysAgo*24*60*60*1000;
		Date nDaysAgoDate = new Date();
		Date nowDate = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
		String end = dateFormat.format(nowDate);
		long endSecond = nowDate.getTime();
		long startSecond = endSecond - nDaysSecond;
		nDaysAgoDate.setTime(startSecond);
		String start = dateFormat.format(nDaysAgoDate);
		
//		List<IDeviceStatistic> src1 = dashBoardDao.getSRC();
//		List<IDeviceStatistic> src1 = dashBoardDao.getSRC1();
//		log.info("***********&&&&&&&&" + src1);
		Object src12 = trafficTrendDao.getSRC12();
		log.info(src12.toString());
		
//		log.info("***********&&&&&&&&" + src1);
		
//		Map<Integer, Long> src = dashBoardDao.getSRC();
//		List<String> src1 = iphdrDao.getSRC1();
//		
//		List<Iphdr> all = iphdrDao.findAll();
		/*
		List<Event> arr = eventDao.query(startDate,endDate);
		for (int i = 0 ;i<arr.length;i++) {
			iphdrDao.query(arr.get(i).getSid(),arr.get(i).getcid());
		}
		*/
		
		ret.put("dev1", "35%");
		ret.put("dev2", "18%");
		ret.put("dev3", "22%");
		ret.put("dev4", "25%");
		return ret;
	}

}
