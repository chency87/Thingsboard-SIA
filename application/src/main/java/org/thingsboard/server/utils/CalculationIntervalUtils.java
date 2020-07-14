package org.thingsboard.server.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;



public class CalculationIntervalUtils {

	/**
	 * 根据时间区间计算时间间隔
	 * 	传入的日期字符串格式为	 yyyy-MM-dd HH:mm:ss
	 * @param startTime
	 * @param endTime
	 * @param xNum(x轴取点个数)
	 */
	  public static ArrayList<Date> getInterval(String startTime, String endTime, int xNum ){
		  
		 ArrayList<Date> intervalList = new ArrayList<Date>();
	     SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	     try {
			Date startDate = dateFormat.parse(startTime);
			Date endDate = dateFormat.parse(endTime);
			long startSecond = startDate.getTime();
			long endSecond = endDate.getTime();
			long second = endSecond - startSecond;  
			if(second < 0) {
				System.out.println("--------------日期输入有误 -------------------");
			}else {
				
				long interval = second/xNum;
			
				for(int nu = 0; nu < xNum; nu++ ) {
					long timeStamp = startSecond + interval*nu;
					Date date = new Date(timeStamp);
					intervalList.add(date);
				}
				
				System.out.println("存入日期的个数" + intervalList.size());
				
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return intervalList;
	     
	   }

	  /**
	   * 根据给定的日期得出起始点的字符串日期  
	   * 输出的日期字符串格式为	 yyyy-MM-dd HH:mm:ss
	   * @param startTime 给定的
	   * @param nDays
	   * @return
	   */
	  public static String[] getStartAndEndTime(String startTime, int nDays ) {
		  
		  return null;
	  }
}
