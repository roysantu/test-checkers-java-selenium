package frameworkBase;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

public class ExtentReportNG {
//	static ExtentTest test;
	static ExtentReports report;
	
	
	public static ExtentReports getExtentReport() {
		
		ExtentSparkReporter extent=new ExtentSparkReporter(System.getProperty("user.dir") + "/reports/index.html");
		extent.config().setDocumentTitle("Web Automation-Extent Report");
		extent.config().setReportName("Web Automation");
		
		report = new ExtentReports();
		report.attachReporter(extent);
		report.setSystemInfo("QA", "Santu Roy");
		
		return report;
	}

}
