package org.example;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import io.github.bonigarcia.wdm.WebDriverManager;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertTrue;

public class ReservasTest {

    private WebDriver driver;
    private WebDriverWait wait;
    private Map<String, Object> vars;

    @Before
    public void setUp() {
        WebDriverManager.firefoxdriver().setup();
        driver = new FirefoxDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        vars = new HashMap<>();
    }

    @After
    public void tearDown() {
        if (driver != null) driver.quit();
    }

    @Test
    public void testReservarMesa() throws InterruptedException {
        driver.get("http://localhost:8080/index.html");
        driver.manage().window().setSize(new Dimension(1355, 729));
        Thread.sleep(1000);

        driver.findElement(By.linkText("Reservas")).click();
        Thread.sleep(1000);

        WebElement comensalesInput = wait.until(ExpectedConditions.elementToBeClickable(By.id("comensales")));
        comensalesInput.click();
        comensalesInput.sendKeys("4");
        Thread.sleep(1000);

        WebElement fechaInput = wait.until(ExpectedConditions.elementToBeClickable(By.id("fecha")));
        fechaInput.click();
        fechaInput.sendKeys(java.time.LocalDate.now().plusDays(1).toString()); // Reserva para mañana
        Thread.sleep(1000);

        WebElement mesaSelect = wait.until(ExpectedConditions.elementToBeClickable(By.id("mesa")));
        mesaSelect.click();
        mesaSelect.findElement(By.cssSelector("option[value='3']")).click(); // Elige mesa 3
        Thread.sleep(1000);

        WebElement btnConfirmar = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("button[type='submit']")));
        btnConfirmar.click();

        // Esperar y comprobar notificación de éxito
        WebElement mensajeExito = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("reservaExito")));
        assertTrue(mensajeExito.isDisplayed());
        Thread.sleep(1500); // Solo para visualización
    }
}