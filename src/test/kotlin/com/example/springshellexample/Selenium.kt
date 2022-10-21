package com.example.springshellexample

import org.assertj.core.api.Assertions.assertThat
import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.stereotype.Service
import org.testcontainers.containers.BrowserWebDriverContainer
import org.testcontainers.containers.VncRecordingContainer
import org.testcontainers.lifecycle.TestDescription
import java.nio.file.Path
import java.util.*

private const val DOCKER_HOST_IP = "172.17.0.1"

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
annotation class SeleniumTest

private const val GREEN = "\u001b[32m"
private const val DEFAULT = "\u001b[0m"

abstract class SeleniumLog {
    fun success(depth: Int, message: String) =
        println("${" ".repeat(depth)}$GREEN$message$DEFAULT")
}

@Service
class Selenium : SeleniumLog() {
    @Autowired
    private lateinit var serverPortService: ServerPortService
    private val container: BrowserWebDriverContainer<*> by lazy {
        return@lazy BrowserWebDriverContainer()
            .withRecordingMode(
                BrowserWebDriverContainer.VncRecordingMode.RECORD_FAILING,
                Path.of("target").toFile(),
                VncRecordingContainer.VncRecordingFormat.MP4
            )
    }
    private val driver: WebDriver by lazy {
        container.start()
        return@lazy container.webDriver
    }

    @Suppress("HttpUrlsUsage")
    private fun get(url: String) = driver.get("http://${DOCKER_HOST_IP}:${serverPortService.getPort()}/$url")

    fun pageTest(url: String, init: PageTest.() -> Unit) {
        container.beforeTest(TestDescriptionInst(url))
        try {
            get(url)
            val pageTest = PageTest(driver, url)
            pageTest.init()
        } catch (e: Throwable) {
            container.afterTest(TestDescriptionInst(url), Optional.of(e))
            throw e
        }
        container.afterTest(TestDescriptionInst(url), Optional.empty())
    }

}

class TestDescriptionInst(private val name: String) : TestDescription {
    override fun getTestId(): String = name
    override fun getFilesystemFriendlyName(): String = name
}
@DslMarker
annotation class SeleniumMarker

@SeleniumMarker
interface PageStep

@SeleniumMarker
class PageTest(private val driver: WebDriver, url: String) :
    SeleniumLog() {
    init {
        success(0, "Page loaded: GET $url")
    }

    fun tagById(id: String, init: TagStep.() -> Unit) =
        TagStep(driver.findElement(By.id(id)))
            .init()

    fun inputById(id: String, init: InputStep.() -> Unit) =
        InputStep(driver.findElement(By.id(id)))
            .init()

    fun findButtonById(id: String, init: ButtonStep.() -> Unit) =
        ButtonStep(driver.findElement(By.id(id)))
            .init()

}

class ButtonStep(private val element: WebElement) : TagStep(element) {
    fun click() {
        element.click()
        success(2, "Click.")
    }

}

class InputStep(private val element: WebElement) : TagStep(element) {
    fun write(text: String) {
        element.sendKeys(text)
        success(2, "Sent keys: $text")
    }
}

open class TagStep(private val element: WebElement) : PageStep, SeleniumLog() {
    init {
        success(1, "Found element: ${element.tagName} (id: ${element.getAttribute("id")})")
    }

    fun assertTextContains(expectedText: String) {
        assertThat(element.text).contains(expectedText)
        success(2, "Contains text: $expectedText")
    }

    fun assertTextNotContains(notExpectedText: String) {
        assertThat(element.text).doesNotContain(notExpectedText)
        success(2, "Not contains text: $notExpectedText")
    }

}