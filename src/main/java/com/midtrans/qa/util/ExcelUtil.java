package com.midtrans.qa.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.codoid.products.exception.FilloException;
import com.codoid.products.fillo.Connection;
import com.codoid.products.fillo.Fillo;
import com.codoid.products.fillo.Recordset;

public class ExcelUtil {
	//
	public static Map<String,String> getSheetDataInMap(String sheetName,String dataID) throws FilloException
	{
		Map<String,String> dataMap = new HashMap<String, String>();
		try {
			
			dataMap.clear();//
						
			Fillo file = new Fillo();
			Connection conn = file.getConnection(System.getProperty("user.dir")+"/src/main/java/com/midtrans/qa/testdata/MidTrans_Test-Data.xlsx");
			if(conn!=null)
			{
				Recordset rs = conn.executeQuery(" Select * from " + sheetName + " where Data_ID='" + dataID + "'");
				
				while(rs.next())
				{
					ArrayList<String> columns = rs.getFieldNames();
					
					for(String col : columns)
					{
						dataMap.put(col, rs.getField(col));
					}
				}
				
			}
		} catch (Exception e) {
			e.getStackTrace();
		}
		
		return dataMap;
	
	}


}
