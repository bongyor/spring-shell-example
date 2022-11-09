package com.example.springshellexample

import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.testcontainers.containers.BrowserWebDriverContainer
import org.testcontainers.containers.VncRecordingContainer
import java.nio.file.Path

private const val DOCKER_HOST_IP = "172.17.0.1"

@Service
class SeleniumDriverService {
    @Autowired
    private lateinit var serverPortService: ServerPortService
    val container: BrowserWebDriverContainer<*> by lazy {
        return@lazy BrowserWebDriverContainer()
            .withRecordingMode(
                BrowserWebDriverContainer.VncRecordingMode.RECORD_FAILING,
                Path.of("target").toFile(),
                VncRecordingContainer.VncRecordingFormat.MP4
            )
    }

    val driver: WebDriver by lazy {
        container.start()
        println("Selenium container started, vnc address: ${container.vncAddress}")
        return@lazy container.webDriver
    }

    @Suppress("HttpUrlsUsage")
    fun get(url: String) = driver.get("http://${DOCKER_HOST_IP}:${serverPortService.getPort()}/$url")
    fun elementById(id: String): WebElement = driver.findElement(By.id(id))
}