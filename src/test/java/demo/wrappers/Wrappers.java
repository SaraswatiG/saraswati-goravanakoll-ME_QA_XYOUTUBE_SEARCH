package demo.wrappers;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import java.time.Duration;
import java.util.List;

public class Wrappers {
    /*
     * Write your selenium wrappers here
     */
    public static void logStatus(String type, String message, String status) {

        System.out.println(String.format("%s |  %s  |  %s | %s",
                String.valueOf(java.time.LocalDateTime.now()), type, message, status));
    }

    public static void clickAction(WebDriver driver, By locator) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
            WebElement buttonElement = wait.until(ExpectedConditions.elementToBeClickable(locator));
            getElementInView(driver, buttonElement);
            buttonElement.click();
        } catch (Exception e) {
            System.out.println("Exception Occured! " + e.getMessage());
        }
    }

    public static void getElementInView(WebDriver driver, WebElement element) {
        // WebElement element = driver.findElement(locator);
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
    }

    public static Boolean waitForPageLoad(WebDriver driver, String title) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        Boolean status = wait.until(ExpectedConditions.urlContains(title));
        return status;
    }

    public static String getElemetText(WebDriver driver, By locator) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        String text = element.getText().trim();
        return text;
    }

    public static String getLastElemetTextInList(WebDriver driver, By locator) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        wait.until(ExpectedConditions.presenceOfElementLocated(locator));
        List<WebElement> list = driver.findElements(locator);
        String text = list.get(list.size()-1).getText().trim();
        return text;
    }


    public static void scrollToRightExtreem(WebDriver driver, By rightClickElement) throws InterruptedException {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement scrollRightButton = wait.until(ExpectedConditions.elementToBeClickable(rightClickElement));
            //WebElement scrollRightButton = driver.findElement(rightClickElement);

            // Scroll by clicking the button until we reach the right extreme
            while (true) {
                // Click the scroll right button
                scrollRightButton.click();
                boolean status = scrollRightButton.isDisplayed();
                if (status) {
                    continue;
                }

                // Wait for a short period to allow the scroll to happen
                Thread.sleep((new java.util.Random().nextInt(3) + 2) * 1000); // Adjust the sleep time as needed

                // Check if at the right extreme
                if (!status) {
                    System.out.println("Reached the right extreme of the scroll bar");
                    break;
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static String getElementFromParentElementAndPrintText(WebDriver driver,WebElement parentElement, By locator,int elementNo){
        WebElement element = parentElement.findElements(locator).get(elementNo);
        String txt = element.getText();
        return txt;
    }

    public static long convertToNumericValue(String value) {
        // Trim the string to remove any leading or trailing spaces
        value = value.trim().toUpperCase();

        // Check if the last character is non-numeric and determine the multiplier
        char lastChar = value.charAt(value.length() - 1);
        int multiplier = 1;
        switch (lastChar) {
            case 'K':
                multiplier = 1000;
                break;
            case 'M':
                multiplier = 1000000;
                break;
            case 'B':
                multiplier = 1000000000;
                break;
            default:
                // If the last character is numeric, parse the entire string
                if (Character.isDigit(lastChar)) {
                    return Long.parseLong(value);
                }
                throw new IllegalArgumentException("Invalid format: " + value);
        }

        // Extract the numeric part before the last character
        String numericPart = value.substring(0, value.length() - 1);
        double number = Double.parseDouble(numericPart);

        // Calculate the final value
        return (long) (number * multiplier);
    }



}
