package com.test.testscripts;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class WebTestcases {
	private WebDriver driver;

	@BeforeClass
	public void setUp() {
		System.setProperty("webdriver.chrome.driver", "path/to/chromedriver");
		driver = new ChromeDriver();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
		driver.manage().window().maximize();
		driver.get("https://test.com/autocomplete-form");
	}

	@AfterClass
	public void tearDown() {
		if (driver != null) {
			driver.quit();
		}
	}

	@Test(priority = 1, description = "Verify input field accepts text")
	public void testInputFieldAcceptsText() {
		WebElement inputField = driver.findElement(By.id("input-field"));
		inputField.sendKeys("test input");
		Assert.assertEquals(inputField.getAttribute("value"), "test input", "Input field did not accept text.");
	}

	@Test(priority = 2, description = "Verify suggestions appear for matching input")
	public void testSuggestionsAppear() {
		WebElement inputField = driver.findElement(By.id("input-field"));
		inputField.clear();
		inputField.sendKeys("agile");

		List<WebElement> suggestions = driver.findElements(By.cssSelector(".suggestions li"));
		Assert.assertTrue(suggestions.size() > 0, "Suggestions list is empty.");
		for (WebElement suggestion : suggestions) {
			Assert.assertTrue(suggestion.getText().toLowerCase().contains("agile"), "Suggestion does not match input.");
		}
	}

	@Test(priority = 3, description = "Verify no suggestions appear for unmatched input")
	public void testNoSuggestionsAppear() {
		WebElement inputField = driver.findElement(By.id("input-field"));
		inputField.clear();
		inputField.sendKeys("xyz");

		List<WebElement> suggestions = driver.findElements(By.cssSelector(".suggestions li"));
		Assert.assertTrue(suggestions.isEmpty(), "Suggestions list should be empty.");
	}

	@Test(priority = 4, description = "Verify valid suggestion selection")
	public void testValidSuggestionSelection() {
		WebElement inputField = driver.findElement(By.id("input-field"));
		inputField.clear();
		inputField.sendKeys("agile");

		WebElement firstSuggestion = driver.findElement(By.cssSelector(".suggestions li"));
		String suggestionText = firstSuggestion.getText();
		firstSuggestion.click();

		Assert.assertEquals(inputField.getAttribute("value"), suggestionText, "Selected suggestion is not populated.");
	}

	@Test(priority = 5, description = "Verify Next button functionality")
	public void testNextButtonFunctionality() {
		WebElement inputField = driver.findElement(By.id("input-field"));
		inputField.clear();
		inputField.sendKeys("agile");

		WebElement firstSuggestion = driver.findElement(By.cssSelector(".suggestions li"));
		firstSuggestion.click();

		WebElement nextButton = driver.findElement(By.id("next-button"));
		nextButton.click();

		WebElement successMessage = driver.findElement(By.cssSelector(".success-container p"));
		Assert.assertTrue(successMessage.isDisplayed(), "Success message is not displayed.");
	}

	@Test(priority = 6, description = "Verify error message for invalid input")
	public void testErrorMessageForInvalidInput() {
		WebElement inputField = driver.findElement(By.id("input-field"));
		inputField.clear();
		inputField.sendKeys("xyz");

		WebElement nextButton = driver.findElement(By.id("next-button"));
		nextButton.click();

		WebElement errorMessage = driver.findElement(By.cssSelector(".error-message"));
		Assert.assertTrue(errorMessage.isDisplayed(), "Error message is not displayed.");
	}

	@Test(priority = 7, description = "Verify tab navigation functionality")
	public void testTabNavigation() {
		WebElement inputField = driver.findElement(By.id("input-field"));
		inputField.click();

		Actions actions = new Actions(driver);
		actions.sendKeys(Keys.TAB).perform(); // Navigate to suggestions
		actions.sendKeys(Keys.DOWN).sendKeys(Keys.ENTER).perform(); // Select first suggestion

		WebElement nextButton = driver.switchTo().activeElement();
		Assert.assertEquals(nextButton.getText(), "Next", "Tab navigation to Next button failed.");
	}

	@Test(priority = 8, description = "Verify clearing input hides suggestions")
	public void testClearingInputHidesSuggestions() {
		WebElement inputField = driver.findElement(By.id("input-field"));
		inputField.sendKeys("agile");
		inputField.clear();

		List<WebElement> suggestions = driver.findElements(By.cssSelector(".suggestions li"));
		Assert.assertTrue(suggestions.isEmpty(), "Suggestions list is not empty after clearing input.");
	}
}
