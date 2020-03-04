package com.midtrans.qa.base;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class CalenderTest {
	
	public static Date getDate(String d) throws ParseException
	{
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		return sdf.parse(d);
	}
	
	public static int getWeekends(String date1,String date2) throws ParseException
	{
		int weekends=0;
		Calendar startDate = Calendar.getInstance();
		Calendar endDate = Calendar.getInstance();
		startDate.setTime(getDate(date1));
		endDate.setTime(getDate(date2));
		
		while(startDate.before(endDate) || startDate.equals(endDate))
		{
			if(startDate.get(Calendar.DAY_OF_WEEK)==Calendar.SUNDAY)
			{
				weekends = weekends+1;
			}
			startDate.add(Calendar.DATE, 1);
		}
		return weekends;
		
	}
	

	public static void main(String[] args) throws Exception {
		System.out.println(getWeekends("18/01/2020","19/01/2020"));

	}

}