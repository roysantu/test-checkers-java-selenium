package pageObjects;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindAll;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.apache.logging.log4j.Logger;

import frameworkBase.FrameworkBase;

public class CheckersPage extends FrameworkBase {

	public WebDriverWait wait = new WebDriverWait(driver, 1000);

	// Elements
	// Header Elements
	@FindBy(xpath = "//h1")
	WebElement header;

	@FindBy(css = "div#board")
	WebElement boar;

	@FindAll({ @FindBy(css = "div#board div.line") })
	public List<WebElement> boardLines;

	@FindBy(css = "p#message")
	WebElement moveStateMessage;

	@FindBy(linkText = "Restart...")
	WebElement lnkRestart;

	@FindAll({ @FindBy(xpath = "//div[@id='board']//img[contains(@src, 'you1')]") })
	public List<WebElement> imgYouPlayers;

	// Constructor
	public CheckersPage(Logger log) {
		PageFactory.initElements(driver, this);
	}

	public Boolean isYourNextTurn() {
		Boolean yourTurn = false;
		wait.until(ExpectedConditions.textToBePresentInElement(this.moveStateMessage, "Make a move."));
		if (moveStateMessage.isDisplayed() && moveStateMessage.getText().equals("Make a move.")) {
			yourTurn = true;
		}
		return yourTurn;
	};
	
	public Boolean isYourFirstTurn() {
		Boolean yourTurn = false;
		if (moveStateMessage.isDisplayed() && moveStateMessage.getText().equals("Select an orange piece to move.")) {
			yourTurn = true;
		}
		return yourTurn;
	};
	
	public WebElement getPawnWithIndex(int elemIndex) {
		WebElement pawnWithIndex = imgYouPlayers.get(elemIndex);
		System.out.println("selected by Index>>>>>>" + pawnWithIndex.getAttribute("name"));

		return pawnWithIndex;
	}

	public WebElement getRandomPawn() {
		Random random = new Random();
		WebElement randomPawn = imgYouPlayers.get(random.nextInt(imgYouPlayers.size()));
		System.out.println("Randomly selected >>>>>>" + randomPawn.getAttribute("name"));

		return randomPawn;
	}

	public PositionObject getPositionObjOfPawn(WebElement elem) {

		String positionName = elem.getAttribute("name");
		String positionStr = positionName.substring(positionName.length() - 2);
		boolean isInt = false;

		if (positionStr.matches("\\d+")) {
			isInt = true;
		}
		PositionObject positionObject = new PositionObject(Integer.parseInt(positionStr.substring(0, 1)),
				Integer.parseInt(positionStr.substring(1)));

		return (positionStr != null && !positionStr.isEmpty() && isInt) ? positionObject : null;
	}

	public boolean ifFieldEmpty(int xColumn, int yRow) {
		String attrName = "space" + xColumn + yRow;
		boolean isEmpty = false;

		WebElement selectedSpace = driver.findElement(By.xpath("//div[@class='line']/img[@name='" + attrName + "']"));

		System.out.println("From isEmpty function <<src>>>>" + selectedSpace.getAttribute("src") + ">>name>>> "
				+ selectedSpace.getAttribute("name"));
		System.out.println(selectedSpace.getAttribute("src").contains("you1.gif") + "<<<>>>>"
				+ selectedSpace.getAttribute("src").contains("me1.gif"));

		
		if (selectedSpace.getAttribute("src").contains("gray.gif")) {
			isEmpty = true;
		} else {
			isEmpty = false;
		}
			
		System.out.println("isEmpty flag val >>>>" + isEmpty);
		return isEmpty;
	}

	public int[] legalMove(WebElement elem) {
		PositionObject elemPosition = getPositionObjOfPawn(elem);
		int[] tempVal = new int[2];

		if (elemPosition != null) {
			System.out.println(
					"Elem position From Object >>>xCol >>" + elemPosition.xCol + ">> yLine >>" + elemPosition.yLine);
			if (elemPosition.xCol < 7 && elemPosition.xCol > 0) {
				if (ifFieldEmpty(elemPosition.xCol + 1, elemPosition.yLine + 1)) {
					tempVal[0] = elemPosition.xCol + 1;
					tempVal[1] = elemPosition.yLine + 1;

				} else if (ifFieldEmpty(elemPosition.xCol - 1, elemPosition.yLine + 1)) {
					tempVal[0] = elemPosition.xCol - 1;
					tempVal[1] = elemPosition.yLine + 1;
				} else {
					System.out.println("isFieldEmpty returning false");
					return null;
				}

			} else if (elemPosition.xCol == 7) {
				if (ifFieldEmpty(elemPosition.xCol - 1, elemPosition.yLine + 1)) {
					tempVal[0] = elemPosition.xCol - 1;
					tempVal[1] = elemPosition.yLine + 1;
				}
				;
			} else if (elemPosition.xCol == 0) {
				if (ifFieldEmpty(elemPosition.xCol + 1, elemPosition.yLine + 1)) {
					tempVal[0] = elemPosition.xCol + 1;
					tempVal[1] = elemPosition.yLine + 1;
				}

			} else {
				System.out.println("No Legal move condition");
				return null;
			}
		}

		System.out.println("Legal move of filtered pawn>>>>>>" + Arrays.toString(tempVal));

		return tempVal;
	}

	public WebElement selectRandomPawnWithLegalMove() {
		WebElement selectedPawn;
		WebElement FilteredPawn;

		do {
//			// TODO
			selectedPawn = getRandomPawn();
			// Check if Pawn has legal move
			if (legalMove(selectedPawn) != null
					&& (legalMove(selectedPawn)[0] != 0 && legalMove(selectedPawn)[1] != 0)) {
				FilteredPawn = selectedPawn;
			} else
				FilteredPawn = null;
		} while (FilteredPawn == null);

		System.out.println("Filtered selected >>>>>>" + selectedPawn.getAttribute("name"));
		return FilteredPawn;
	}

	public void clickLegalPosition(int[] legalMove) {
		WebElement selectedSpace = driver
				.findElement(By.xpath("//div[@class='line']/img[@name='space" + legalMove[0] + legalMove[1] + "']"));
		selectedSpace.click();
	}
	
	public void clickLegalPosition(WebElement elem, int[] legalMove) {
		driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
		elem.click();
		driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
		if(!elem.getAttribute("src").contains("you2.gif") || moveStateMessage.getText().equals("Please wait.")) {
			elem.click();
		}
		
		WebElement selectedSpace = driver
				.findElement(By.xpath("//div[@class='line']/img[@name='space" + legalMove[0] + legalMove[1] + "']"));
		selectedSpace.click();
		
		if(!selectedSpace.getAttribute("name").endsWith(""+legalMove[0] + legalMove[1])) {
			selectedSpace.click();
		}
		
	}

	public void makeMoves(int moveCount) {
		WebElement selectedPawn = selectRandomPawnWithLegalMove();
		int[] moveArray = legalMove(selectedPawn);
		clickLegalPosition(selectedPawn, moveArray);
		driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
		
//		wait.until(ExpectedConditions.textToBePresentInElement(this.moveStateMessage, "Make a move."));

	}
	
	public void makeFirstMove(WebElement elem, int xCol, int yLine) {
		wait.until(ExpectedConditions.textToBePresentInElement(this.moveStateMessage, "Select an orange piece to move."));
		elem.click();
		String attrName = "space" + xCol + yLine;
		WebElement selectedSpace = driver.findElement(By.xpath("//div[@class='line']/img[@name='" + attrName + "']"));
		selectedSpace.click();
		driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
		Assert.assertTrue(selectedSpace.getAttribute("src").contains("you2.gif"));
	}
	
	public void makeNextMove(WebElement elem, int xCol, int yLine) {
		wait.until(ExpectedConditions.textToBePresentInElement(this.moveStateMessage, "Make a move."));
		driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
		elem.click();
		String attrName = "space" + xCol + yLine;
		WebElement selectedSpace = driver.findElement(By.xpath("//div[@class='line']/img[@name='" + attrName + "']"));
		driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
		if(elem.getAttribute("src").contains("you2.gif")) {
			selectedSpace.click();
		} else {
			elem.click();
			driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
			selectedSpace.click();
		}
		
		
	}
	
	public void restartTheGame() {
		lnkRestart.click();
		wait.until(ExpectedConditions.textToBePresentInElement(this.moveStateMessage, "Select an orange piece to move."));
		
	}

}

class PositionObject {
	int xCol;
	int yLine;

	PositionObject(int xCol, int yLine) {
		this.xCol = xCol;
		this.yLine = yLine;
	}

}
