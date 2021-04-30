package com.example.consumingrest;

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

public class MovieSearchAPITests {

	private WebDriver driver;
	private WebDriverWait wait;

	@BeforeEach
	public void openBrowser() {
		System.setProperty("webdriver.chrome.driver", "src/test/resources/chromedriver");
		driver = new ChromeDriver();
		wait = new WebDriverWait(driver, 2000);
	}

	@AfterEach
	public void closeBrowser() {
		driver.quit();
	}

	@Test
	public void shouldSearchMoviesForTitleAndShowAListWithAllMoviesContainingIt() throws InterruptedException {
		searchMovieAuxiliarMethod("star wars");

		wait.until(ExpectedConditions.presenceOfElementLocated(By.className("Card")));

		WebElement movie1 = driver
				.findElement(By.xpath("//*[@id=\"root\"]/div/div/main/div/div[2]/div/div/div[1]/div/div[1]/div[1]"));
		WebElement movie2 = driver
				.findElement(By.xpath("//*[@id=\"root\"]/div/div/main/div/div[2]/div/div/div[2]/div/div[1]/div[1]"));
		WebElement movie3 = driver
				.findElement(By.xpath("//*[@id=\"root\"]/div/div/main/div/div[2]/div/div/div[3]/div/div[1]/div[1]"));

		assertEquals(movie1.getText(), "Star Wars: Episode IV - A New Hope");
		assertEquals(movie2.getText(), "Star Wars: Episode V - The Empire Strikes Back");
		assertEquals(movie3.getText(), "Star Wars: Episode VI - Return of the Jedi");
	}

	@Test
	public void shoudClickOnMovieAndShowDetails() {
		searchMovieAuxiliarMethod("star wars");

		WebElement expectedElement = wait.until(ExpectedConditions.presenceOfElementLocated(By.className("Card")));

		expectedElement.click();

		WebElement movieContent = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("movieDetailsContent")));

		String title = movieContent.findElement(By.id("movieDetailsTitle")).getText();
		String year = movieContent.findElement(By.id("movieDetailsYear")).getText();
		String genre = movieContent.findElement(By.id("movieDetailsGenre")).getText();

		assertEquals(title, "Star Wars: Episode IV - A New Hope");
		assertEquals(year, "1977");
		assertEquals(genre, "Genre: Action, Adventure, Fantasy, Sci-Fi");
	}

	private void searchMovieAuxiliarMethod(String title) {
		driver.get("http://localhost:3000/");
		driver.manage().window().setSize(new Dimension(960, 1080));

		WebElement searchBar = driver.findElement(By.id("homeSearchBar"));
		searchBar.click();
		searchBar.sendKeys(title);

		WebElement searchButton = driver.findElement(By.id("homeSearchButton"));
		searchButton.click();
	}

}
