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

public class ContactoTest {

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
    public void testEnviarFormularioContacto() throws InterruptedException {
        driver.get("http://localhost:8080/index.html");
        driver.manage().window().setSize(new Dimension(1355, 729));
        Thread.sleep(1000);

        driver.findElement(By.linkText("Contáctanos")).click();
        Thread.sleep(1000);

        WebElement nombreInput = wait.until(ExpectedConditions.elementToBeClickable(By.id("nombre")));
        nombreInput.click();
        nombreInput.sendKeys("Juan Pérez");
        Thread.sleep(1000);

        WebElement emailInput = wait.until(ExpectedConditions.elementToBeClickable(By.id("email")));
        emailInput.click();
        emailInput.sendKeys("juan@example.com");
        Thread.sleep(1000);

        WebElement asuntoInput = wait.until(ExpectedConditions.elementToBeClickable(By.id("asunto")));
        asuntoInput.click();
        asuntoInput.sendKeys("Quisiera reservar para una celebración especial.");
        Thread.sleep(1000);

        WebElement btnEnviar = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("button[type='submit']")));
        btnEnviar.click();

        // Esperar y comprobar notificación de éxito
        WebElement mensajeExito = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("mensajeExito")));
        assertTrue(mensajeExito.isDisplayed());
        Thread.sleep(1500); // Solo para visualización, no necesario en una prueba real
    }
}