package frameworkBase;

import java.io.IOException;

import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;

public class Listener extends FrameworkBase implements ITestListener {

	static ExtentTest test;
	ExtentReports report = ExtentReportNG.getExtentReport();
	ThreadLocal<ExtentTest> extentTest = new ThreadLocal<ExtentTest>();

	@Override
	public void onTestStart(ITestResult result) {
		// TODO Auto-generated method stub
		String testMethodName = result.getMethod().getMethodName();
		test = report.createTest(testMethodName);
		extentTest.set(test);

		extentTest.get().log(Status.INFO, "Starting Test : " + testMethodName);

	}

	@Override
	public void onTestSuccess(ITestResult result) {
		// TODO Auto-generated method stub

		WebDriver driver = null;
		Logger log = null;
		String screenshotPaths = "";
		

		String testMethodName = result.getMethod().getMethodName();

		extentTest.get().log(Status.PASS, "Test : " + testMethodName + " Passed");
//		extentTest.get().pass(result.getThrowable()); // TODO remove later if not needed

		try {
			driver = (WebDriver) result.getTestClass().getRealClass().getDeclaredField("driver")
					.get(result.getInstance());
			log = (Logger) result.getTestClass().getRealClass().getDeclaredField("log").get(result.getInstance());

			// for multiple screenshots
			screenshotPaths = (String) result.getTestClass().getRealClass().getDeclaredField("screenshotPaths")
					.get(result.getInstance());

			//

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			// Take Screenshots
			// for multiple screenshots
//			if (screenshotPaths != null & !screenshotPaths.isEmpty()) {
//				pathArray = screenshotPaths.split(";");
//				System.out.println(screenshotPaths);
//				System.out.println(pathArray[0]);
//				System.out.println(pathArray.length);
//				for (String eachPath : pathArray) {
//					extentTest.get().addScreenCaptureFromPath(eachPath, testMethodName);
//				}
//			}
			getOnDemandScreenshots(screenshotPaths, testMethodName);
			
			extentTest.get().addScreenCaptureFromPath(getScreenshot(testMethodName, driver), testMethodName);
			log.info(result.getTestName() + " - " + result.isSuccess());

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void onTestFailure(ITestResult result) {
		// TODO Auto-generated method stub
		WebDriver driver = null;
		Logger log = null;
		String screenshotPaths = "";

		String testMethodName = result.getMethod().getMethodName();

		extentTest.get().log(Status.FAIL, "Test : " + testMethodName + " Failed");
		extentTest.get().fail(result.getThrowable());

		try {
			driver = (WebDriver) result.getTestClass().getRealClass().getDeclaredField("driver")
					.get(result.getInstance());
			log = (Logger) result.getTestClass().getRealClass().getDeclaredField("log").get(result.getInstance());

			// for multiple screenshots
						screenshotPaths = (String) result.getTestClass().getRealClass().getDeclaredField("screenshotPaths")
								.get(result.getInstance());
						
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// Take Screenshots
		try {
			
			getOnDemandScreenshots(screenshotPaths, testMethodName);
			
			String screenshotPath = getScreenshot(testMethodName, driver);
			extentTest.get().addScreenCaptureFromPath(screenshotPath, testMethodName);

			log.error(result.getThrowable());

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void onTestSkipped(ITestResult result) {
		// TODO Auto-generated method stub
		extentTest.get().log(Status.SKIP, "Going to skip this test");

	}

	@Override
	public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStart(ITestContext context) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onFinish(ITestContext context) {
		// TODO Auto-generated method stub
		extentTest.get().log(Status.INFO, "Finishing Test");
		report.flush();

	}
	
	public void getOnDemandScreenshots(String screenshotPaths, String testMethodName) {
		String[] pathArray;
		
		if (screenshotPaths != null && !screenshotPaths.isEmpty()) {
			pathArray = screenshotPaths.split(";");
//			System.out.println(screenshotPaths);
//			System.out.println(pathArray[0]);
//			System.out.println(pathArray.length);
			for (String eachPath : pathArray) {
				extentTest.get().addScreenCaptureFromPath(eachPath, testMethodName);
			}
		}
	}

}
