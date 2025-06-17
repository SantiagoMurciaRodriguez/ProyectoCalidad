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

public class ProyectoCalidadTest {

    private WebDriver driver;
    private WebDriverWait wait;
    private Map<String, Object> vars;
    JavascriptExecutor js;

    @Before
    public void setUp() {
        WebDriverManager.firefoxdriver().setup();
        driver = new FirefoxDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        js = (JavascriptExecutor) driver;
        vars = new HashMap<>();
    }

    @After
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    public void testAgregarYEditarPlato() throws InterruptedException {
        // Ahora accedemos al index actualizado
        driver.get("http://localhost:8080/index.html");
        driver.manage().window().setSize(new Dimension(1355, 729));
        Thread.sleep(1000);

        // El index actual ya NO tiene los desplegables ni los acordeones anteriores,
        // así que eliminamos esas líneas.

        // Ahora accedemos al Menú desde la navbar
        driver.findElement(By.linkText("Menú")).click();
        Thread.sleep(1000);

        WebElement nombreInput = wait.until(ExpectedConditions.elementToBeClickable(By.id("nombre")));
        nombreInput.click();
        Thread.sleep(400);
        nombreInput.sendKeys("pollo");
        Thread.sleep(400);

        WebElement descripcionInput = wait.until(ExpectedConditions.elementToBeClickable(By.id("descripcion")));
        descripcionInput.click();
        Thread.sleep(400);
        descripcionInput.sendKeys("pollo");
        Thread.sleep(400);

        WebElement imagenInput = wait.until(ExpectedConditions.elementToBeClickable(By.id("imagen")));
        imagenInput.click();
        Thread.sleep(400);
        imagenInput.sendKeys("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcS1TfeWRgY2ekpPB2om4uu9ZDjoaAnWnpkC4A&s");
        Thread.sleep(400);

        WebElement btnAgregar = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(".btn-success")));
        btnAgregar.click();
        Thread.sleep(1200);

        // Si hay modal backdrop de Bootstrap, espera a que desaparezca
        try {
            wait.until(ExpectedConditions.invisibilityOfElementLocated(By.className("modal-backdrop")));
        } catch (TimeoutException e) {
            // Si no hay modal-backdrop, continúa
        }

        WebElement btnEditar1 = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".col-md-4:nth-child(1) .btn-warning")));

        js.executeScript("arguments[0].scrollIntoView({block: 'center'});", btnEditar1);
        Thread.sleep(500);

        wait.until(ExpectedConditions.elementToBeClickable(btnEditar1)).click();
        Thread.sleep(900);

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".btn-close"))).click();
        Thread.sleep(900);
    }
}