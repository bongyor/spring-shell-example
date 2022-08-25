package com.example.springshellexample.tesztform

import com.example.springshellexample.SeleniumDriver
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

private const val DOCKER_HOST_IP = "172.17.0.1"

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
internal class TesztFormControllerTest {

    @Autowired
    private lateinit var tesztFormController: TesztFormController


    @Test
    fun tesztForm() {
        val driver = SeleniumDriver.getDriver()
        driver.get(tesztFormController.tesztFormUrl(DOCKER_HOST_IP))
        driver
            .findById("cimsor")
            .assertTextContains("Teszt form")
    }

}