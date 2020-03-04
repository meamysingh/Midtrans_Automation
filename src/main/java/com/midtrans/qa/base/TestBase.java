package com.midtrans.qa.base;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.ElementNotInteractableException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.events.EventFiringWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Reporter;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.midtrans.qa.util.TestUtil;
import com.midtrans.qa.util.WebEventListener;

import io.github.bonigarcia.wdm.WebDriverManager;

public class TestBase {
	
	public static WebDriver driver;
	public static Properties prop;
	public static EventFiringWebDriver e_driver;
	public static WebEventListener eventListener;
	public ExtentReports reporter;
	public static ExtentTest logger;
	
	
	
	public TestBase(){
		try {
			prop = new Properties();
			FileInputStream ip = new FileInputStream(System.getProperty("user.dir")+ "/src/main/java/com/midtrans"
					+ "/qa/config/config.properties");
			prop.load(ip);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	/**========================================================================================================================
     * @Name: setupExtentReport
     * @description: setting up the extent report file
     * @param NA 
     * @paramType NA
     * @return - Void
     * @author Amit Singh
     *========================================================================================================================*/
	public void setupExtentReport()
	{
		File file = new File(System.getProperty("user.dir")+"/Execution-Summary-Report.html");
		ExtentHtmlReporter extentHtmlReporter = new ExtentHtmlReporter(file);
		extentHtmlReporter.config().setDocumentTitle("TEST AUTOMATION REPORT");
		extentHtmlReporter.config().setReportName("MIDTRANS TEST EXECUTION REPORT");
		reporter = new ExtentReports();
		reporter.attachReporter(extentHtmlReporter);
		
		System.out.println("report setup has been done");
	}

	/**========================================================================================================================
     * @Name: initialization
     * @description: setting up the extent report file
     * @param NA 
     * @paramType NA
     * @return - Void
     * @author Amit Singh
     *========================================================================================================================*/
	public static void initialization(){
		try {
			
			String browserName = prop.getProperty("browser");
			
			if(browserName.equalsIgnoreCase("chrome")){
				
				WebDriverManager.chromedriver().setup();
				driver = new ChromeDriver(); 
			}
			else if(browserName.equalsIgnoreCase("FF")){
				WebDriverManager.firefoxdriver().setup();
				driver = new FirefoxDriver(); 
			}
			else if(browserName.equalsIgnoreCase("FF")) {
				WebDriverManager.iedriver().setup();
				driver = new InternetExplorerDriver();
			}
			
			e_driver = new EventFiringWebDriver(driver);
			// Now create object of EventListerHandler to register it with EventFiringWebDriver
			eventListener = new WebEventListener();
			e_driver.register(eventListener);
			driver = e_driver;
			
			driver.manage().window().maximize();
			driver.manage().deleteAllCookies();
			driver.manage().timeouts().pageLoadTimeout(TestUtil.PAGE_LOAD_TIMEOUT, TimeUnit.SECONDS);
			driver.manage().timeouts().implicitlyWait(TestUtil.IMPLICIT_WAIT, TimeUnit.SECONDS);
			
			driver.get(prop.getProperty("url"));
			
		} catch (Exception e) {
			e.getStackTrace();
		}
		
		
	}
	
	/**========================================================================================================================
     * @Name: waitForIsDisplayed
     * @description: waits for element to be displayed on UI
     * @param element- webelement to check on UI 
     * @paramType WebElement
     * @param timeout- time for waiting
     * @paramType long
     * @return - boolean
     * @author Amit Singh
     *========================================================================================================================*/
	public boolean waitForIsDisplayed(WebElement element,long timeout)
	{
		try {
			WebDriverWait wait = new WebDriverWait(driver, timeout);
			wait.until(ExpectedConditions.visibilityOf(element));
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	
	/**========================================================================================================================
     * @Name: textOf
     * @description: gets the text of a specified element
     * @param element- webElement to perform action on UI
     * @paramType WebElement
     * @return - String
     * @author Amit Singh
     *========================================================================================================================*/
	public String textOf(WebElement element)
	{
		String text = null;
		try {
			waitForIsDisplayed(element, 10);
			text = element.getText();
			Reporter.log("Successfuly fetched the text: "+text,true);
			
		} catch (NoSuchElementException noSuch) {
			Reporter.log("not able to fetch text: "+text,true);
			text = null;
		}
		return text;
	}
	
	public int getOnlyDigints(String alphanumericString)
	{
		   String numberOnly= alphanumericString.replaceAll("[^0-9]", "");
		   return Integer.parseInt(numberOnly);
	}
	
	/**========================================================================================================================
     * @Name: actionOnCheckbox
     * @description: performes check/uncheck on checkbox as per the input given
     * @param element- checkbox element on ui
     * @paramType WebElement
     * @param actionType- check/uncheck
     * @paramType STring
     * @return - void
     * @author Amit Singh
     *========================================================================================================================*/
	public void actionOnCheckbox(WebElement element,String actionType)
	{
		hoverOver(element);
		switch(actionType.toUpperCase())
		{
		case "CHECK":
			if(element.isSelected())
			{
				Reporter.log("checkbox is already selected",true);
			}else {
				
				Reporter.log("checkbox is not selected",true);
			    element.click();

			}
		break;
		
		case "UNCHECK":
		     
			if(element.isSelected())
			{
				Reporter.log("checkbox is already selected",true);
				element.click();
			}else {
			
				Reporter.log("checkbox is not selected",true);

			}
	    break;
		
		}
	}
	
	
	/**========================================================================================================================
     * @Name: attributeOf
     * @description: gets the attribute value of an element on ui
     * @param attributeName- attribute name to find value
     * @paramType String
     * @param element- WebElement
     * @paramType WebElement
     * @return - String
     * @author Amit Singh
     *========================================================================================================================*/
	public String attributeOf(String attributeName, WebElement element)
	{
		String attributeVal = null;
		try {
			waitForIsDisplayed(element, 10);
			attributeVal = element.getAttribute(attributeName);
			Reporter.log("Successfuly fetched the attribute value: "+attributeVal,true);
			
		} catch (NoSuchElementException noSuch) {
			Reporter.log("not able to fetch text: "+attributeVal,true);
			attributeVal = null;
		}
		return attributeVal;
	}
	

	/**========================================================================================================================
     * @Name: getElementByText
     * @description: finds the element by text and tagname specified
     * @param tagName- tahName to build xpath
     * @paramType String
     * @param text- visible text to build xpath
     * @paramType String
     * @return - WebElement
     * @author Amit Singh
     *========================================================================================================================*/
	public WebElement getElementByText(String tagName,String text)
	{
		return driver.findElement(By.xpath("//"+tagName+"[text()='"+text+"']"));
	}
	
	
	/**========================================================================================================================
     * @Name: clickOnElement
     * @description: clicks on specified webelement using click method of selenium
     * @param element- element on which action will be performed
     * @paramType WebElement
     * @return - boolean
     * @author Amit Singh
     *========================================================================================================================*/
	public boolean clickOnElement(WebElement element)
	{
				try {
					
				hoverOver(element);
				waitForIsClickable(element, 10);
				element.click();
				logger.log(Status.INFO, "Successfuly clicked on button");
				return true;	
					
				} catch (NoSuchElementException Nosuch) {
					logger.log(Status.FAIL, "The element is not clickable at this moment");
					Nosuch.getStackTrace();
					return false;
				}catch (Exception e) {
					logger.log(Status.FAIL, "The element is not clickable at this moment");
					e.getStackTrace();
					return false;
				}
	}
	
	
	/**========================================================================================================================
     * @Name: hoverOver
     * @description: moves the cursor over element specified
     * @param ele- element on which action will be performed
     * @paramType WebElement
     * @return - void
     * @author Amit Singh
     *========================================================================================================================*/
	public void hoverOver(WebElement ele)
	{
		try {
			Actions action = new Actions(driver);
			action.moveToElement(ele).perform();
			
		} catch (ElementNotInteractableException  e) {
			logger.log(Status.FAIL, "either the element is not visible or interactable at the moment");
			
		}
	}
	
	/**========================================================================================================================
     * @Name: waitForIsClickable
     * @description: waits for element to be enabled on ui
     * @param element- webelement to check on UI 
     * @paramType WebElement
     * @param timeout- time for waiting
     * @paramType long
     * @return - voids
     * @author Amit Singh
     *========================================================================================================================*/
	public void waitForIsClickable(WebElement ele,long timeout)
	{
			WebDriverWait wait = new WebDriverWait(driver, timeout);
			wait.until(ExpectedConditions.elementToBeClickable(ele));
	}
	
	
	/**========================================================================================================================
     * @Name: type
     * @description: enters value on UI using sendKeys method of selenium
     * @param valueToBeENtered- String to be entered
     * @paramType String
     * @param element- element on which action will be performed
     * @paramType WebElement
     * @return - void
     * @author Amit Singh
     *========================================================================================================================*/
	public void type(String valueToBeENtered,WebElement element)
	{
		try {
			waitForIsDisplayed(element, 10);
			hoverOver(element);
			element.clear();
			element.sendKeys(valueToBeENtered);
			logger.log(Status.INFO, "Successfuly have set the value = "+valueToBeENtered+" in the required field");
		} catch (NoSuchElementException e) {
			logger.log(Status.FAIL, "either the element is not visible at the moment");
		}catch (Exception e) {
			logger.log(Status.FAIL, "either the element is not visible at the moment");
		}
	}
	
	
	
	
	
	
	
	
	
	

}
