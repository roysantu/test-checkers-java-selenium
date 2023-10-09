package frameworkBase;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.testng.Reporter;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

import io.github.bonigarcia.wdm.WebDriverManager;

public class FrameworkBase {

	public static WebDriver driver;
	public static Properties prop;
	public String executionContext="";

	public FrameworkBase() {

		try {
			prop = new Properties();
			FileInputStream fis = new FileInputStream(System.getProperty("user.dir") + "/resources/data.properties");
			prop.load(fis); // Read data from Property file
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public WebDriver initializeDriver() {

		String browserName = prop.getProperty("browserName");
		WebDriverManager.chromedriver().setup();

		if (browserName.equals("chrome")) {
			driver = WebDriverManager.chromedriver().create();

		} else if (browserName.equals("firefox")) {
			// TODO

		} else if (browserName.equals("safari")) {
			// TODO
		} else driver = WebDriverManager.chromedriver().create();

//		driver.manage().window().maximize();
		driver.manage().deleteAllCookies();
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

		return driver;

	}

	public String getScreenshot(String testMethodName, WebDriver driver) throws IOException {
		TakesScreenshot ts;
		if(executionContext.equals("")) {
			ts = (TakesScreenshot) driver;
		} else {
			// TODO Fix alert screenshot
			ts = (TakesScreenshot) driver.switchTo().alert();
		}
		
		File source = ts.getScreenshotAs(OutputType.FILE);
		String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
		String destinationFile = System.getProperty("user.dir") + "/reports/screenshots/" + testMethodName + "_"
				+ timeStamp + ".png";

		FileUtils.copyFile(source, new File(destinationFile));
		return destinationFile;
	}

	public String captureScreenshot(String screenshotPaths, String testMethodName) {

		try {
			if (screenshotPaths != null && !screenshotPaths.isEmpty()) {
				screenshotPaths = screenshotPaths + ";" + getScreenshot(testMethodName, driver);
			} else {
				screenshotPaths = getScreenshot(testMethodName, driver);
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return screenshotPaths;
	}
	// TODO Fix alert screenshot
	public String captureScreenshot(String screenshotPaths, String testMethodName, String mode) {

		if (mode.equals("alert")) {
			executionContext = mode;
			driver.switchTo().alert();
		}
		try {
			if (screenshotPaths != null && !screenshotPaths.isEmpty()) {
				screenshotPaths = screenshotPaths + ";" + getScreenshot(testMethodName, driver);
			} else {
				screenshotPaths = getScreenshot(testMethodName, driver);
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return screenshotPaths;
	}

}
