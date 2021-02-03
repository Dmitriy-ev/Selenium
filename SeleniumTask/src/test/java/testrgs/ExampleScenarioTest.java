package testrgs;


import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

public class ExampleScenarioTest {

	private WebDriver driver;
	private WebDriverWait wait;


	@Before
	public void before() {

		System.setProperty("webdriver.chrome.driver", "webdriver/chromedriver.exe");

		ChromeOptions options = new ChromeOptions();
		options = new ChromeOptions();
		options.addArguments("--disable-notifications");

		driver = new ChromeDriver(options);
		driver.manage().window().maximize();
		driver.manage().timeouts().pageLoadTimeout(50, TimeUnit.SECONDS);
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

		wait = new WebDriverWait(driver, 10, 1000);

		String baseUrl = "https://www.rgs.ru";
		driver.get(baseUrl);

		new WebDriverWait(driver, 1).until(webDriver -> ((JavascriptExecutor) webDriver)
				.executeScript("return document.readyState").equals("complete"));
	}

	@Test
	public void exampleScenario() throws InterruptedException {
		//выбрать меню.
		String menuButtonXPath = "//a[@class='hidden-xs' and contains(text(), 'Меню')]";
		List<WebElement> menuButtonList = driver.findElements(By.xpath(menuButtonXPath));
		if(!menuButtonList.isEmpty())
			menuButtonList.get(0).click();

		//выбрать пункт - Компаниям
		String companyButtonXPath = "//a[contains(text(), 'Компаниям')]";
		WebElement companyButton = driver.findElement(By.xpath(companyButtonXPath));
		companyButton.click();

		//выбрать пункт - Здоровье
		String healthButtonXPath = "//a[@class='list-group-item adv-analytics-navigation-line4-link' and contains(text(), 'Здоровье')]";
		WebElement healthButton = driver.findElement(By.xpath(healthButtonXPath));
		waitUtilElementToBeClickable(healthButton);
		healthButton.click();

		//выбрать пунк - добровольное медицинское страхование
		String dmsButtonXPath = "//a[contains(text(), 'Добровольное медицинское')]";
		WebElement dmsButton = driver.findElement(By.xpath(dmsButtonXPath));
		waitUtilElementToBeClickable(dmsButton);
		dmsButton.click();

		//проверка заголовка - добровольное медицинское страхование
		String dmsTitleXPath = "//a[contains(text(), 'Добровольное медицинское')]";
		waitUtilElementToBeVisible(By.xpath(dmsTitleXPath));
		WebElement dmsTitle = driver.findElement(By.xpath(dmsTitleXPath));
		Assert.assertEquals("Заголовок отсутствует/не соответствует требуемому",
				"Добровольное медицинское страхование", dmsTitle.getText());
		//заявка
		String sendButtonXPath = "//a[contains(text(), 'Отправить заявку')]";
		WebElement sendButton = driver.findElement(By.xpath(sendButtonXPath));
		waitUtilElementToBeClickable(sendButton);
		sendButton.click();

		//проверка открытия заявки на добровольное медицинское страхование
		String applicationTitleXPath = "//b";
		waitUtilElementToBeVisible(By.xpath(applicationTitleXPath));
		WebElement applicationTitle = driver.findElement(By.xpath(applicationTitleXPath));
		Assert.assertEquals("Заголовок отсутствует/не соответствует требуемому",
				"Заявка на добровольное медицинское страхование", applicationTitle.getText());

		// заполнить поля данными
		String fieldXPath = "//*[@name='%s']";
		fillInputField(driver.findElement(By.xpath(String.format(fieldXPath, "LastName"))), "Васильев");
		fillInputField(driver.findElement(By.xpath(String.format(fieldXPath, "FirstName"))), "Иван");
		fillInputField(driver.findElement(By.xpath(String.format(fieldXPath, "MiddleName"))), "Александрович");
		Select dprCountry = new Select(driver.findElement(By.xpath("//*[contains(@name,'Region')]")));
		dprCountry.selectByVisibleText("Москва");
		fillInputField(driver.findElement(By.xpath("//*[contains(text(),'Телефон')]/../child::input")), "+7 (779) 167-62-71");
		fillInputField(driver.findElement(By.xpath(String.format(fieldXPath, "Email"))), "qwertyqwerty");
		fillInputField(driver.findElement(By.xpath(String.format(fieldXPath, "ContactDate"))), "05.02.1987" + "\n");
		fillInputField(driver.findElement(By.xpath(String.format(fieldXPath, "Comment"))), "Я согласен на обработку");


		//клик по галочке
		String personalDataButtonXPath = "//*[contains(@type,'checkbox')]/../child::label[contains(text(),'Я согласен')]";
		WebElement personalDataButton = driver.findElement(By.xpath(personalDataButtonXPath));
		waitUtilElementToBeClickable(personalDataButton);
		personalDataButton.click();


		//клик отправить
		String sendButtonXPath1 = "//button[contains(text(),'Отправить')]";
		WebElement sendButton1 = driver.findElement(By.xpath(sendButtonXPath1));
		waitUtilElementToBeClickable(sendButton1);
		sendButton1.click();

		//проверка email
		String errorEmailXPath = "//span[@class='validation-error-text']";
		WebElement errorEmail = driver.findElement(By.xpath(errorEmailXPath));
		waitUtilElementToBeVisible(errorEmail);
		Assert.assertEquals("Проверка ошибки у validation на странице не была пройдена",
				"Введите адрес электронной почты", errorEmail.getText());
	}

	@After

	public void after(){
		driver.quit();
	}

	private void scrollToElementJs(WebElement element) {
		JavascriptExecutor javascriptExecutor = (JavascriptExecutor) driver;
		javascriptExecutor.executeScript("arguments[0].scrollIntoView(true);", element);
	}

	private void waitUtilElementToBeClickable(WebElement element) {
		wait.until(ExpectedConditions.elementToBeClickable(element));
	}

	private void waitUtilElementToBeVisible(By locator) {
		wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
	}

	private void waitUtilElementToBeVisible(WebElement element) {
		wait.until(ExpectedConditions.visibilityOf(element));
	}

	private void fillInputField(WebElement element, String value) {
		scrollToElementJs(element);
		waitUtilElementToBeClickable(element);
		element.click();
		element.sendKeys(value);
	}

}
