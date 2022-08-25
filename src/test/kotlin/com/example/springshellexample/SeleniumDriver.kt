package com.example.springshellexample

import org.assertj.core.api.Assertions.assertThat
import org.openqa.selenium.By
import org.openqa.selenium.WebElement
import org.openqa.selenium.remote.RemoteWebDriver
import org.testcontainers.containers.BrowserWebDriverContainer
import org.testcontainers.containers.VncRecordingContainer
import java.nio.file.Path

class SeleniumDriver(private val driver: RemoteWebDriver) {
    fun get(url: String) = driver.get(url)
    fun findTagById(id: String) = SeleniumElement(driver.findElement(By.id(id)))
    fun findInputById(id: String) = SeleniumInput(driver.findElement(By.id(id)))
    fun findButtonById(id: String) = SeleniumButton(driver.findElement(By.id(id)))

    companion object {
        private var instance: SeleniumDriver? = null
        fun getDriver(): SeleniumDriver {
            initDriver()
            return instance!!
        }

        private fun initDriver() {
            if (instance == null) {
                val container = BrowserWebDriverContainer()
                    .withRecordingMode(
                        BrowserWebDriverContainer.VncRecordingMode.RECORD_ALL,
                        Path.of(".").toFile(),
                        VncRecordingContainer.VncRecordingFormat.MP4
                    )
                container.start()
                instance = SeleniumDriver(container.webDriver)
            }
        }
    }
}

class SeleniumButton(private val element: WebElement) {
    fun click() = element.click()
}

class SeleniumInput(private val element: WebElement) {
    fun write(text: String) {
        element.sendKeys(text)
    }

}

class SeleniumElement(private val element: WebElement) {
    fun assertTextContains(expectedText: String) {
        assertThat(element.text).contains(expectedText)
    }
}
