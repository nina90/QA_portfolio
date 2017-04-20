
package TestToHome;

import static org.junit.Assert.*;

import java.awt.FileDialog;
import java.awt.Frame;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import org.junit.runners.MethodSorters;
import org.junit.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import com.gargoylesoftware.htmlunit.javascript.host.file.File;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;
import com.sun.jna.platform.unix.X11.Display;


@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class Basket_testing {

	static ExtentReports report;
	static ExtentTest logger;
	static WebDriver driver;
	static String arg0;

	@BeforeClass
	public static void SetUp()
	{
		// path where to save the report
		arg0 = "C:\\Users\\TOTA\\Documents\\eclipse_workspace\\TEMP\\TOHOME_COM_UA_Cart_Testing_Report.html";

		report = new ExtentReports(arg0);

		System.setProperty("webdriver.chrome.driver", "C:\\Users\\TOTA\\Documents\\eclipse_workspace\\BrowserDrivers\\chromedriver_win32\\chromedriver.exe");
		driver=new ChromeDriver();
		driver.manage().window().maximize();

		// add more system info to report
		report.addSystemInfo("ExtentReport Version", "2.41.2");
		report.addSystemInfo("Selenium Version", "3.3");
		report.addSystemInfo("User Name", "Antonina Semerenko");
		report.addSystemInfo("Host Name", "Laptop");

	}

	@Test
	public void Test1_AddingItemsToCart () throws InterruptedException
	{
		String productName 			= "ЗАЧАРОВАНИЙ ЛІС";
		boolean flg_find_product 	= false;

		logger = report.startTest("Verify adding one item to basket");

		driver.get("https://tohome.com.ua/");
		logger.log(LogStatus.INFO, "Application is up and running");

		driver.findElement(By.xpath("//a[contains(text(),'Книгарня')]")).click();
		logger.log(LogStatus.INFO, "Книгарня tab was clicked");

		driver.findElement(By.xpath("//div[@class = 'productinfo text-center']/a[contains(@title,'Книги для дітей')]")).click();
		logger.log(LogStatus.INFO, "Книги для дітей link was clicked");

		driver.findElement(By.xpath("//a[contains(text(),'Розмальовки')]")).click();
		logger.log(LogStatus.INFO, "Розмальовки link was clicked");
		Thread.sleep(3000);

		driver.findElement(By.xpath("//div[contains(@data-product-name,'" + productName + "')]//button[@class='add-to-cart btn']")).click();
		logger.log(LogStatus.INFO, "Item was added to cart");
		Thread.sleep(5000);

		List <WebElement> itemsNameInCart = driver.findElements(By.xpath("//td[@class = 'product product-name shopping-cart-cell']"));

		for (WebElement curItem : itemsNameInCart)
		{
			if (curItem.getText().contains(productName))
			{	
				flg_find_product = true;
				break;
			}
		}

		assertTrue(flg_find_product);
		logger.log(LogStatus.INFO, "Product was added to cart successfully");
		String sPath = Utility.captureScreenshot(driver, "Test1_AddingItemsToCart_Screenshot1");
		String iName = logger.addScreenCapture(sPath);
		logger.log(LogStatus.PASS, "The '" + productName + "' product is stored in the shopping cart",iName);

	}

	@Test
	public void Test2_AddingMoreItemsToCart () throws InterruptedException
	{

		String[] productName 			= {"ЗАЧАРОВАНИЙ ЛІС", "Hamster Яблоко"};
		boolean[] flg_find_product 		= {false,false};

		logger = report.startTest("Verify adding multiple items to basket, the cart should not be empty) ");

		driver.get("https://tohome.com.ua/");
		logger.log(LogStatus.INFO, "Application is up and running");

		driver.findElement(By.xpath("//a[contains(text(),'Зоотовари')]")).click();
		logger.log(LogStatus.INFO, "Зоотовари tab was clicked");

		driver.findElement(By.xpath("//div[@class = 'productinfo text-center']/a[contains(@title,'Утримання тварин')]")).click();
		logger.log(LogStatus.INFO, "Утримання тварин link was clicked");

		driver.findElement(By.xpath("//div[@class = 'productinfo text-center']/a[contains(@title,'Туалети')]")).click();
		logger.log(LogStatus.INFO, "Туалети link was clicked");

		driver.findElement(By.xpath("//div[@class = 'pagination']//li[contains(@class,'next-page')]")).click();
		logger.log(LogStatus.INFO, "Go to next page");

		driver.findElement(By.xpath("//div[contains(@data-product-name,'Hamster Яблоко, 2 кг')]//button[@class='add-to-cart btn']")).click();
		logger.log(LogStatus.INFO, "Second item was added to cart");

		Thread.sleep(2000);

		List <WebElement> itemsNameInCart = driver.findElements(By.xpath("//td[@class = 'product product-name shopping-cart-cell']"));

		for (WebElement curItem : itemsNameInCart)
		{
			for (int i = 0; i < productName.length; i++)
			{
				if (curItem.getText().contains(productName[i]))
				{	
					flg_find_product[i] = true;
				}
			}
		}

		for (int i = 0; i < flg_find_product.length; i++)
		{
			assertTrue(flg_find_product[i]);
		}

		String sPath = Utility.captureScreenshot(driver, "Test2_AddingMoreItemsToCart_screenshot1");
		String iName = logger.addScreenCapture(sPath);
		logger.log(LogStatus.PASS, "All selected items were added to cart",iName);

	}



	@Test
	public void Test3_CheckingProccedToCheckout () throws InterruptedException
	{
		String sPath,iName;
		int newQuantity[] = {4,5};

		logger = report.startTest("Checking proceed to checkout after user done with shopping");

		driver.get("https://tohome.com.ua/");
		logger.log(LogStatus.INFO, "Application is up and running");
		
		driver.findElement(By.xpath("//span[@class = 'cart-label']")).click();
		logger.log(LogStatus.INFO, "Go to the shoping cart");

		// change quantity
		int item1Price = Integer.parseInt(   driver.findElement(By.xpath("//tbody[@class='shopping-cart-tbody']/tr[1]/td/span[@class = 'product-unit-price']")).getText().replaceAll("\\D+","")   );
		int item2Price = Integer.parseInt(   driver.findElement(By.xpath("//tbody[@class='shopping-cart-tbody']/tr[2]/td/span[@class = 'product-unit-price']")).getText().replaceAll("\\D+","")   );

		int ExpectedTotal = item1Price * newQuantity[0] + item2Price * newQuantity[1];

		WebElement e1 = driver.findElement(By.xpath("//tbody[@class='shopping-cart-tbody']/tr[1]//input[@id = 'quantity-input']"));
		e1.clear();
		e1.sendKeys(   Integer.toString(  newQuantity[0]  )    );

		WebElement e2 = driver.findElement(By.xpath("//tbody[@class='shopping-cart-tbody']/tr[2]//input[@id = 'quantity-input']"));
		e2.clear();
		e2.sendKeys(Integer.toString(newQuantity[1]));

		logger.log(LogStatus.INFO, "Items quantity was modified");

		driver.findElement(By.xpath("//div[@class='common-buttons']/button[@id = 'checkout']")).click();
		
		sPath = Utility.captureScreenshot(driver, "Test3_CheckingProccedToCheckout_screenshot1");
		iName = logger.addScreenCapture(sPath);
		logger.log(LogStatus.INFO, "Shoping Cart",iName);
		
		driver.findElement(By.xpath("//div[@class='common-buttons']/button[@id = 'checkout']")).click();

		int actualTotal = Integer.parseInt(     driver.findElement(By.xpath("//div[@class='total_area']/ul/li[1]/span")).getText().replaceAll("\\D+","")      );

		boolean compRez = ExpectedTotal == actualTotal;

		assertFalse(compRez);

		sPath = Utility.captureScreenshot(driver, "Test3_CheckingProccedToCheckout_screenshot2");
		iName = logger.addScreenCapture(sPath);
		logger.log(LogStatus.INFO, "Expected Total: " + ExpectedTotal + " Actual Total: " + actualTotal);
		logger.log(LogStatus.ERROR, "Total calculation is wrong",iName);

	}

	@After
	public void AfterTest()
	{
		report.endTest(logger);
	}

	@AfterClass
	public static void TearDown()

	{
		report.flush();
		// driver.quit();
		driver.get("file:///" + arg0);
	}
}
