package com.example.springshellexample

import org.assertj.core.api.Assertions.assertThat
import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.testcontainers.containers.BrowserWebDriverContainer
import org.testcontainers.containers.VncRecordingContainer
import java.nio.file.Path

private const val DOCKER_HOST_IP = "172.17.0.1"
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
annotation class SeleniumTest

@SpringBootTest
class SeleniumTestBase {
    @Autowired
    private lateinit var serverPortService: ServerPortService
    private val driver get() = getDriver()
    @Suppress("HttpUrlsUsage")
    fun get(url: String) = driver.get("http://${DOCKER_HOST_IP}:${serverPortService.getPort()}/$url")
    fun findTagById(id: String) = SeleniumElement(driver.findElement(By.id(id)))
    fun findInputById(id: String) = SeleniumInput(driver.findElement(By.id(id)))
    fun findButtonById(id: String) = SeleniumButton(driver.findElement(By.id(id)))

    companion object {
        private var driverSingleton: WebDriver? = null
        fun getDriver(): WebDriver {
            initDriver()
            return driverSingleton!!
        }

        private fun initDriver() {
            if (driverSingleton == null) {
                val container = BrowserWebDriverContainer()
                    .withRecordingMode(
                        BrowserWebDriverContainer.VncRecordingMode.RECORD_ALL,
                        Path.of(".").toFile(),
                        VncRecordingContainer.VncRecordingFormat.MP4
                    )
                container.start()
                driverSingleton = container.webDriver
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

    fun assertTextNotContains(notExpectedText: String) {
        assertThat(element.text).doesNotContain(notExpectedText)
    }
}
