package com.midtrans.qa.testcases;

import java.io.IOException;

import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.aventstack.extentreports.MediaEntityBuilder;
import com.codoid.products.exception.FilloException;
import com.midtrans.qa.base.TestBase;
import com.midtrans.qa.pages.MidTrans_Home_Page;
import com.midtrans.qa.util.TestUtil;

public class MIdTrans_Home_Test extends TestBase {
	
	TestUtil testUtil;
	MidTrans_Home_Page midTransObj;
	
	//calls parent class constructor to initialize property file
	public MIdTrans_Home_Test()
	{
		super();
		
    }
	
	//initialize report
	@BeforeTest
	public void setupReport() {
		setupExtentReport();
	}
	
	//launch the browser
	@BeforeMethod
	public void setUp() throws InterruptedException {
		
		initialization();
//		testUtil = new TestUtil();
		midTransObj = new MidTrans_Home_Page();
	}

	//Test-1, make successful payment
	@Test(priority = 0)
	public void validate_SuccessFul_Order() throws FilloException, InterruptedException {
		logger = reporter.createTest("validate_SuccessFul_Order");
		midTransObj.place_Successful_Order();
	}
	
	//Test-2, make unsuccessful Payment
	@Test(priority = 1)
	public void Validate_UnSuccessful_Order() throws FilloException, InterruptedException
	{
		logger = reporter.createTest("Validate_UnSuccessful_Order");
		midTransObj.place_UnSuccessful_Order();
	}
	
	//closed the browser and report file
	@AfterMethod
	public void tearDown(ITestResult result) throws IOException
	{
		
		if(result.getStatus()==ITestResult.FAILURE)
		{
			String temp=TestUtil.takeScreenshotAtEndOfTest();
			
			logger.fail(result.getThrowable().getMessage(), MediaEntityBuilder.createScreenCaptureFromPath(temp).build());
		}else if (result.getStatus()==ITestResult.FAILURE)
		{
            String temp=TestUtil.takeScreenshotAtEndOfTest();
			
			logger.fail(result.getThrowable().getMessage(), MediaEntityBuilder.createScreenCaptureFromPath(temp).build());
		}
		
		reporter.flush();
		driver.quit();
		
	}
	
}
