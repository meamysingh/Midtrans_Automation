package com.midtrans.qa.pages;

import java.util.List;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.Reporter;

import com.aventstack.extentreports.Status;
import com.codoid.products.exception.FilloException;
import com.midtrans.qa.base.TestBase;
import com.midtrans.qa.util.ExcelUtil;
import com.midtrans.qa.util.TestUtil;

public class MidTrans_Home_Page extends TestBase {
	String productName;
	String productPrice;
	//page factory OR
	
	@FindBy(linkText="BUY NOW")
	private WebElement buyNowButton;
	
	@FindBy(xpath="//span[contains(text(),'Shopping Cart')]")
	private WebElement ShoppingCart;
	
	@FindBy(xpath="//div[@class='cart-content buying']//table")
	private List<WebElement> tables;
	
	@FindBy(xpath="//a[@class='button-main-content']")
	private WebElement continueButton;
	
	@FindBy(xpath ="//input[@type='checkbox' and @name='promo']")
	private WebElement promoCheckbox;
	
	@FindBy(xpath = "//span[@class='text-amount-amount']")
	private WebElement ammountAtPayment;
	
	@FindBy(xpath = "(//span[text()='Select Promo']/../../..//span)[2]")
	private WebElement promoAmmountFld;
	
	@FindBy(name = "PaRes")
	private WebElement otpTextFld;

	@FindBy(name = "ok")
	private WebElement OKButtonAtPaymentConfirmation;
	
	@FindBy(xpath="//td[text()='Address']/..//textarea")
	private WebElement addressTextField;
	
	@FindBy(id="snap-midtrans")
	private WebElement webFrame;
	
	@FindBy(xpath="//div[@class='text-failed text-bold']")
    private WebElement failedTransactionAlert;
    
	/**========================================================================================================================
     * @Name: getFieldByTtitle
     * @description: returns a webelement for the title provided in function
     * @param title - title of the field
     * @return - WebElement
     * @author Amit SIngh
     *========================================================================================================================*/
	public WebElement getFieldByTtitle(String title)
	{
		return driver.findElement(By.xpath("//td[text()='"+title+"']/..//input"));
	}
	
	
	/**========================================================================================================================
     * @Name: getCustomisedElement
     * @description: returns a webelement for the fieldName provided in function
     * @param fieldName - fieldName of the element
     * @paramType String
     * @return - WebElement
     * @author Amit Singh
     *========================================================================================================================*/
	public WebElement getCustomisedElement(String fieldName)
	{
		return driver.findElement(By.xpath("//label[text()='"+fieldName+"']/..//input"));
	}
	
	//Initializing the Page Objects:
	
		public MidTrans_Home_Page(){
			PageFactory.initElements(driver, this);
		}
		
		
		/**========================================================================================================================
	     * @Name: place_Successful_Order
	     * @description: does the successful payment as per the data provided
	     * @param NA 
	     * @paramType NA
	     * @return - Void
	     * @author Amit Singh
	     *========================================================================================================================*/
		public void place_Successful_Order() throws FilloException, InterruptedException
		{
			if(waitForIsDisplayed(buyNowButton,10))
			{
				productName = textOf(getElementByText("div","Midtrans Pillow"));
				productPrice = textOf(getElementByText("span","20,000"));
				
				buyNowButton.click();
				if(validateCart())
				{
					Map<String, String> params = ExcelUtil.getSheetDataInMap("User_Details", "1");
					//
					type(params.get("Name"), getFieldByTtitle("Name"));
					type(params.get("Email"), getFieldByTtitle("Email"));
					type(params.get("Phone_No"), getFieldByTtitle("Phone no"));
					type(params.get("City"), getFieldByTtitle("City"));
					type(params.get("Address"), addressTextField);
					type(params.get("Postal_Code"), getFieldByTtitle("Postal Code"));
					
					clickOnElement(getElementByText("div", "CHECKOUT"));
					TestUtil.switchToFrame(webFrame);
					if(waitForIsDisplayed(getElementByText("span", "shipping details"), 10))
					{
						logger.log(Status.INFO, "Order summary page has been displayed");
						clickOnElement(getElementByText("span", "shipping details"));
						
						logger.log(Status.INFO, "order summary data matches with user details");
						clickOnElement(continueButton);
						waitForIsDisplayed(getElementByText("p", "Select Payment"), 10);
						placePayment(ExcelUtil.getSheetDataInMap("Payment_Details", "1").get("Payment method"),"1");
						driver.switchTo().defaultContent();
						if(waitForIsDisplayed(getElementByText("span", "Thank you for your purchase."), 15))
						{
							logger.pass("The order has been place successfully with the Success message: "+textOf(getElementByText("span", "Thank you for your purchase.")));
						}
					}
					
				}
				else
				{
					logger.log(Status.FAIL, "value has not been updated as per the product product detail");
				}
			}
		}
		
		/**========================================================================================================================
	     * @Name: placePayment
	     * @description: makes the payment as per the input provided
	     * @param paymentMethod- credit card
	     * @paramType String
	     * @param dataId- sheet data id
	     * @paramType String
	     * @return - Void
	     * @author Amit Singh
	     *========================================================================================================================*/
		public void placePayment(String paymentMethod,String dataId) throws FilloException, InterruptedException
		{
			String discount = null;
			Map<String, String> params = ExcelUtil.getSheetDataInMap("Payment_Details", dataId);
			WebElement paymentType = driver.findElement(By.xpath("//div[@id='payment-list']//div[text()='"+paymentMethod+"']"));
			discount = attributeOf("class",driver.findElement(By.xpath("//div[@id='payment-list']//div[text()='"+paymentMethod+"']/../..")));
			if(discount.contains("promo"))
			{
				logger.log(Status.INFO, "The payment Method: "+paymentMethod+" has promo code attched to it");
				clickOnElement(paymentType);
				
				waitForIsDisplayed(promoCheckbox, 10);
				if(validatePromoCodeFUnctionality())
				{
					type(params.get("Cart Number"), getCustomisedElement("Card number"));
					type(params.get("Expiry date"), getCustomisedElement("Expiry date"));
					type(params.get("CVV"), getCustomisedElement("CVV"));
					clickOnElement(continueButton);
					Thread.sleep(3000);
      				TestUtil.switchInMultipleFrame(otpTextFld);
					waitForIsDisplayed(getElementByText("h1", "Issuing Bank"), 15);
					type(params.get("OTP"), otpTextFld);
					logger.log(Status.INFO, "successfully entered OTP as= "+params.get("OTP"));
					clickOnElement(OKButtonAtPaymentConfirmation);
					logger.log(Status.INFO, "clicked on OK button for payment confirmation");
					
				}
				
				
			}
			else {
				logger.log(Status.INFO, "The payment Method: "+paymentMethod+" doest not have any promo code attched to it");
			}
		
		}
		
		/**========================================================================================================================
	     * @Name: validatePromoCodeFUnctionality
	     * @description: validates the promo code functionality applied on perticular payment method
	     * @param NA
	     * @paramType NA
	     * @return - boolean- returns true if promo functionality working fine
	     * @author Amit Singh
	     *========================================================================================================================*/
		public boolean validatePromoCodeFUnctionality()
		{
			int promoVal = 0;
			int totalAmount= 0;
			boolean status = false;
			
			actionOnCheckbox(promoCheckbox,"Uncheck");
			waitForIsDisplayed(ammountAtPayment, 10);
			promoVal = getOnlyDigints(textOf(promoAmmountFld));
			totalAmount = getOnlyDigints(textOf(ammountAtPayment));
			if(getOnlyDigints(productPrice)==totalAmount)
			{
				status = true;
				logger.log(Status.PASS, "The promotion calculation is working fine when promo code is not applied, Total ammount is: "+totalAmount);
			}else
			{
				status = false;
				logger.log(Status.FAIL, "The promotion calculation is not working fine,when promo code is not applied, Total amount is: "+totalAmount);
			}
			
			actionOnCheckbox(promoCheckbox,"check");
			waitForIsDisplayed(ammountAtPayment, 10);
			promoVal = getOnlyDigints(textOf(promoAmmountFld));
			totalAmount = getOnlyDigints(textOf(ammountAtPayment));
			if(getOnlyDigints(productPrice)-promoVal==totalAmount)
			{
				status = true;
				logger.log(Status.PASS, "The promotion calculation is working fine when promo code is applied, Total ammount is: "+totalAmount);
			}else
			{
				status = false;
				logger.log(Status.FAIL, "The promotion calculation is not working fine,when promo code is applied, Total amount is: "+totalAmount);
			}
			
			
			return status;
		}
	
		/**========================================================================================================================
	     * @Name: validateCart
	     * @description: validates the cart items
	     * @param NA
	     * @paramType NA
	     * @return - boolean- returns true if cart is working fine
	     * @author Amit Singh
	     *========================================================================================================================*/
	public boolean validateCart()
	{
		boolean status = false;
		int count =0;
		waitForIsDisplayed(ShoppingCart, 10);
		List<WebElement> cartElements = driver.findElements(By.xpath("//th/../../..//td"));
		
		for(WebElement ele : cartElements)
		{
			if(textOf(ele).equalsIgnoreCase(productName) || textOf(ele).equalsIgnoreCase(productPrice))
				{
					status =true;
					count++;
					Reporter.log("The value in cart is same as product details: "+textOf(ele),true);
					logger.log(Status.PASS, "The value in cart is same as product details: "+textOf(ele));
					if(count ==2)
						break;
				}
				else
				{
					status = false;
					Reporter.log("The value for this cell is: "+textOf(ele));
				}
		}
		return status;
	}
	

	/**========================================================================================================================
     * @Name: place_UnSuccessful_Order
     * @description: does the Unsuccessful payment as per the data provided
     * @param NA 
     * @paramType NA
     * @return - Void
     * @author Amit Singh
     *========================================================================================================================*/
	public void place_UnSuccessful_Order() throws FilloException, InterruptedException
	{
		if(waitForIsDisplayed(buyNowButton,10))
		{
			productName = textOf(getElementByText("div","Midtrans Pillow"));
			productPrice = textOf(getElementByText("span","20,000"));
			
			buyNowButton.click();
			
			if(validateCart())
			{
				Map<String, String> params = ExcelUtil.getSheetDataInMap("User_Details", "2");
				//
				type(params.get("Name"), getFieldByTtitle("Name"));
				type(params.get("Email"), getFieldByTtitle("Email"));
				type(params.get("Phone_No"), getFieldByTtitle("Phone no"));
				type(params.get("City"), getFieldByTtitle("City"));
				type(params.get("Address"), addressTextField);
				type(params.get("Postal_Code"), getFieldByTtitle("Postal Code"));
				
				clickOnElement(getElementByText("div", "CHECKOUT"));
				TestUtil.switchToFrame(webFrame);
				if(waitForIsDisplayed(getElementByText("span", "shipping details"), 10))
				{
					logger.log(Status.INFO, "Order summary page has been displayed");
					clickOnElement(getElementByText("span", "shipping details"));
					
					logger.log(Status.INFO, "order summary data matches with user details");
					clickOnElement(continueButton);
					waitForIsDisplayed(getElementByText("p", "Select Payment"), 10);
					placePayment(ExcelUtil.getSheetDataInMap("Payment_Details", "2").get("Payment method"),"2");
					TestUtil.switchToFrame(webFrame);
					if(waitForIsDisplayed(failedTransactionAlert, 15));
					{
						logger.pass("The Payment has been declied with the failuer message: "+textOf(driver.findElement(By.xpath("//div[@class='text-failed']//span"))));
					}
					
				}
				
			}
			else
			{
				logger.log(Status.FAIL, "value has not been updated as per the product product detail");
			}
		}
	}
}
