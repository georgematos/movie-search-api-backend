package com.example.consumingrest.functional;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class MovieSeachMainPageTest {

	private WebDriver driver;

	@BeforeEach
	public void openBrowser() {
		System.setProperty("webdriver.chrome.driver", "src/test/resources/chromedriver");
		driver = new ChromeDriver();
	}

	@AfterEach
	public void closeBrowser() {
		driver.quit();
	}

	@Test
	public void searchingMovies() throws InterruptedException {
		driver.get("http://localhost:3000/");
		driver.manage().window().setSize(new Dimension(960, 1080));

		WebElement searchBar = driver.findElement(By.xpath("//*[@id=\"root\"]/div/div/main/div/div[1]/input"));
		searchBar.click();
		searchBar.sendKeys("star wars");

		WebElement searchButton = driver.findElement(By.cssSelector(".btn-primary"));
		searchButton.click();

		WebDriverWait wait = new WebDriverWait(driver, 2000);
		WebElement expectedElement = wait.until(ExpectedConditions.presenceOfElementLocated(By.className("Card")));

		expectedElement.click();

		WebElement movieContent = wait
				.until(ExpectedConditions.presenceOfElementLocated(By.className("movie-content")));

		assertEquals(movieContent.findElement(By.xpath("//*[@id=\"root\"]/div/div/main/div/div[1]/h1")).getText(),
				"Star Wars: Episode IV - A New Hope");
		driver.quit();
	}

}
