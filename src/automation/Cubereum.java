package automation;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

public class Cubereum {

	public static void openAmazonPortal(WebDriver driver) {

		driver.get("https://amazon.in");
		driver.manage().window().maximize();
	}

	public static void searchProduct(WebDriver driver) throws InterruptedException {
		//Enter Product name in search box
		driver.findElement(By.xpath("//input[@id='twotabsearchtextbox']")).sendKeys("wrist watches");
		driver.findElement(By.xpath("//input[@id='twotabsearchtextbox']")).sendKeys(Keys.ENTER);
		Thread.sleep(5000);
	}

	public static void filteroutProducts(WebDriver driver, JavascriptExecutor js) throws InterruptedException
	{
		//Select Analogue as Watch Display Type
		driver.findElement(By.xpath("(//span[contains(text(),'Analogue')])[1]")).click();
		driver.manage().timeouts().implicitlyWait(50, TimeUnit.SECONDS);		

		Thread.sleep(5000);
		//Select Brand Material- Leather
		WebElement materialType = driver.findElement(By.xpath("//span[contains(text(),'Leather')]"));
		js.executeScript("arguments[0].click();", materialType);

		Thread.sleep(5000);

		//Select Brands-Titan		
		try {
			WebElement seeMore = driver.findElement(By.xpath("//span[@class='a-expander-prompt']"));
			System.out.println("SEEMORE:- "+seeMore.isDisplayed());
			if(seeMore.isDisplayed()) {
				js.executeScript("arguments[0].click();", seeMore);
				WebElement titanName =driver.findElement(By.xpath("(//span[contains(text(),'Titan')])[1]")); 
				js.executeScript("arguments[0].click();", titanName);
				driver.manage().timeouts().implicitlyWait(50, TimeUnit.SECONDS);						
			}
			else
			{
				try {
					WebElement titanName =driver.findElement(By.xpath("//div[@id='brandsRefinements']//span[contains(text(),'Titan')]")); 
					if(titanName.isDisplayed()) {
						js.executeScript("arguments[0].click();", titanName);
						driver.manage().timeouts().implicitlyWait(50, TimeUnit.SECONDS);
					}
					else
					{
						driver.findElement(By.xpath("(//div[@id='reviewsRefinements']//ul/li)[1]")).click();
						Thread.sleep(4000);
						js.executeScript("arguments[0].click();", titanName);
						Thread.sleep(4000);

					}

				} catch (Exception e) {
					// TODO: handle exception
				}			

			}

		} catch (NoSuchElementException e) {
			// TODO: handle exception
			try {
				WebElement titanName =driver.findElement(By.xpath("//div[@id='brandsRefinements']//span[contains(text(),'Titan')]")); 
				if(titanName.isDisplayed()) {
					js.executeScript("arguments[0].click();", titanName);
					driver.manage().timeouts().implicitlyWait(50, TimeUnit.SECONDS);
				}

			} catch (Exception e1) {
				// TODO: handle exception				
				//Click on rating to make titan appear.
				driver.findElement(By.xpath("(//div[@id='reviewsRefinements']//ul/li)[1]")).click();
				Thread.sleep(4000);
				WebElement titanName =driver.findElement(By.xpath("//div[@id='brandsRefinements']//span[contains(text(),'Titan')]"));
				js.executeScript("arguments[0].click();", titanName);
				Thread.sleep(4000);
				//Clear the rating to suffice the condition.
				WebElement clearText = driver.findElement(By.xpath("//div[@id='reviewsRefinements']//span[@class='a-size-base a-color-base'][contains(text(),'Clear')]")); 
				js.executeScript("arguments[0].click();", clearText);
				Thread.sleep(4000);
			}			

		}
		Thread.sleep(5000);
		//Select Discounts- 25% or more
		driver.findElement(By.xpath("//span[contains(text(),'25% Off or more')]")).click();		
	}


	public static void printandWriteproductinfotoExcel(WebDriver driver) throws IOException, InterruptedException
	{
		Thread.sleep(5000);
		String toSPlit = driver.findElement(By.xpath("//span[contains(text(),'results for')]")).getText().toString();

		String[] arrSplit = toSPlit.split("results");
		if(arrSplit[0].contains("-"))
		{
			String[] arrsplit1 = arrSplit[0].split("of");
			String[] arrsplit2 = arrsplit1[0].split("-");
			System.out.println(arrsplit2[1]);

			try {
				for(int i=0;i<Integer.parseInt(arrsplit2[1].trim());i++) {
					String text = driver.findElement(By.xpath("//span[@class='celwidget slot=SEARCH_RESULTS template=SEARCH_RESULTS widgetId=search-results index="+i+"']//div[3]")).getText().toString();		
					String price = driver.findElement(By.xpath("//span[@class='celwidget slot=SEARCH_RESULTS template=SEARCH_RESULTS widgetId=search-results index="+i+"']//div[4]")).getText().toString();
					String[] arrSplit123 = text.split("\\R");
					String[] arrSplit1234 = price.split("\\R");
					//					System.out.println("Product details:-" +text);
					System.out.println("Name of Product is ==> "+arrSplit123[0]+" "+arrSplit123[1]);
					System.out.println("Price of Product is==> "+arrSplit1234[0].substring(1));

					String[] valueToWrite = {arrSplit123[0]+" "+arrSplit123[1],arrSplit1234[0].substring(1)};

					//Create an object of class which has excel operation code i.e. ExcelWriteOperation

					ExcelWriteOperation objExcelFile = new ExcelWriteOperation();

					//Write the file using file name, sheet name and the data to be filled

					objExcelFile.writeExcel(System.getProperty("user.dir")+"\\src\\automation","ExportExcel.xlsx","EXCELOPERATION",valueToWrite);

					System.out.println("\n");
				}
			} catch (NumberFormatException nfe) {
				nfe.printStackTrace();
			}
		}
		else
		{
			System.out.println(arrSplit[0]);
			try {
				System.out.println("Total products are "+Integer.parseInt(arrSplit[0].trim()));
				for(int i=0;i<(Integer.parseInt(arrSplit[0].trim()));i++) {

					String text = driver.findElement(By.xpath("//span[@class='celwidget slot=SEARCH_RESULTS template=SEARCH_RESULTS widgetId=search-results index="+i+"']//div[3]")).getText().toString();		
					String price = driver.findElement(By.xpath("//span[@class='celwidget slot=SEARCH_RESULTS template=SEARCH_RESULTS widgetId=search-results index="+i+"']//div[4]")).getText().toString();		
					//					System.out.println("Prod Text is :- "+text);
					String[] arrSplit123 = text.split("\\R");
					String[] arrSplit1234 = price.split("\\R");
					if(arrSplit123[1].isEmpty()){
						System.out.println("Name of Product is ==> "+arrSplit123[0]);

						System.out.println("Price of Product is==> "+arrSplit1234[0].substring(1));
						String[] valueToWrite = {arrSplit123[0],arrSplit1234[0].substring(1)};

						//Create an object of current class

						ExcelWriteOperation objExcelFile = new ExcelWriteOperation();

						//Write the file using file name, sheet name and the data to be filled

						objExcelFile.writeExcel(System.getProperty("user.dir")+"\\src\\automation","ExportExcel.xlsx","EXCELOPERATION",valueToWrite);


						System.out.println("\n");
					}
					else
					{	
						System.out.println("Name of Product is ==> "+arrSplit123[0]+" "+arrSplit123[1]);

						System.out.println("Price of Product is==> "+arrSplit1234[0].substring(1));
						String[] valueToWrite = {arrSplit123[0]+" "+arrSplit123[1],arrSplit1234[0].substring(1)};

						//Create an object of current class

						ExcelWriteOperation objExcelFile = new ExcelWriteOperation();

						//Write the file using file name, sheet name and the data to be filled

						objExcelFile.writeExcel(System.getProperty("user.dir")+"\\src\\automation","ExportExcel.xlsx","EXCELOPERATION",valueToWrite);


						System.out.println("\n");

					}
				}
			} catch (NumberFormatException nfe) {
				nfe.printStackTrace();
			}
		}
		Thread.sleep(5000);
	}
	//This method return 10th product on the product listing page. You can call this method when control is on 
	//product listing page. This method will return the Nth Product name.
	public static String getNthProductInformation(WebDriver driver, int n)
	{
		String nthProductInformation = 	driver.findElement(By.xpath("//span[@class='celwidget slot=SEARCH_RESULTS template=SEARCH_RESULTS widgetId=search-results index="+n+"']//div[3])")).getText().toString();
		String[] arrSplit123 = nthProductInformation.split("\\R");
		nthProductInformation = arrSplit123[0];
		return nthProductInformation;
	}

	public static void main(String[] args) throws InterruptedException, IOException {
		// TODO Auto-generated method stub
		System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir")+"\\Resources\\chromedriver.exe");
		WebDriver driver = new ChromeDriver();
		JavascriptExecutor js = (JavascriptExecutor) driver;

		//Open Amazon Portal
		openAmazonPortal(driver);

		//Search the product
		searchProduct(driver);

		/*
		 * Filter out the product with below filters:- 
		1. Analogue
		2. Brand Material- Leather
		3. Brands-Titan	
		4. Discounts- 25% or more
		 */		
		filteroutProducts(driver,js);

		//Print Product info in console and write data to excel.
		printandWriteproductinfotoExcel(driver);

		//Close and quite the driver. 	
		driver.close();		
	}

}
