package testSuite;

import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import frameworkBase.FrameworkBase;
import pageObjects.CheckersPage;


public class CheckersTest extends FrameworkBase {
	public WebDriver driver;
	CheckersPage checkersPage;

	public Logger log = LogManager.getLogger(CheckersTest.class.getName());

	static ExtentTest test;
	static ExtentReports report;

	// for multiple screenshots
	public String screenshotPaths = "";
	//

	@BeforeClass
	public void startTest() {
		System.out.println("Starting checker test class");
		log.info("Starting checker test class");
	}

	@AfterClass
	public void endTest() {
		System.out.println("Ending checker test class");
	}

	public CheckersTest() {
		super();
	}

	@BeforeTest
	public void testSetup() {

	}

	@BeforeMethod
	public void testCaseSetup() {
		driver = initializeDriver();

		checkersPage = new CheckersPage(log);
		driver.get("https://www.gamesforthebrain.com/game/checkers/");
//		ops.openUrl("https://www.gamesforthebrain.com/game/checkers/");

	}

	@AfterMethod
	public void testMethodEnd() throws InterruptedException {
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		driver.close();
		driver.quit();
		log.info("Driver is Closed");

		screenshotPaths = "";
	}

	@Test(enabled = false)
	public void testMetaData() {
//		ops.verifyPageTitle("Checkers - Games for the Brain");
	}

	@Test(enabled = true)
	public void makeRandomFiveMoves() {
		Assert.assertTrue(checkersPage.isYourFirstTurn(), "You're not ready to move");
		
		for (int i = 0; i < 5; i++) {
			int round = i+1;
			System.out.println("Trying Player Round# " + round);
			checkersPage.makeMoves(3);
		}
		checkersPage.restartTheGame();
		Assert.assertTrue(checkersPage.isYourFirstTurn(), "You're not ready to move");
		
	}
	
	@Test(enabled = false)
	public void makeDefinedFiveMoves() {

		Assert.assertTrue(checkersPage.isYourFirstTurn(), "You're not ready to move");
		checkersPage.makeFirstMove(checkersPage.getPawnWithIndex(0), 7, 3);
		Assert.assertTrue(checkersPage.isYourNextTurn(), "You're not ready to move");
		checkersPage.makeNextMove(checkersPage.getPawnWithIndex(5), 6, 2);
		Assert.assertTrue(checkersPage.isYourNextTurn(), "You're not ready to move");
		checkersPage.makeNextMove(checkersPage.getPawnWithIndex(8), 5, 1);
		Assert.assertTrue(checkersPage.isYourNextTurn(), "You're not ready to move");
		checkersPage.makeNextMove(checkersPage.getPawnWithIndex(2), 1, 3);
		Assert.assertTrue(checkersPage.isYourNextTurn(), "You're not ready to move");
		checkersPage.makeNextMove(checkersPage.getPawnWithIndex(6), 2, 2);

	}

}
