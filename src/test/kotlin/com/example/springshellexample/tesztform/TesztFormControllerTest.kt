package com.example.springshellexample.tesztform

import com.example.springshellexample.Selenium
import com.example.springshellexample.SeleniumDriverService
import com.example.springshellexample.SeleniumTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import org.springframework.beans.factory.annotation.Autowired
import java.lang.Exception

@SeleniumTest
internal class TesztFormControllerTest {


    @Autowired
    private lateinit var seleniumDriverService: SeleniumDriverService

    @Autowired
    private lateinit var selenium: Selenium

    @Test
    @DisplayName("Teszt form kipróbálása")
    internal fun tesztFormDsl() {
        selenium.pageTest("tesztform") {
            tagById("cimsor") { assertTextContains("Teszt form") }
            inputById("tesztform.felhasznalonev") { write("Felhasználónév") }
            inputById("tesztform.szam") { write("12") }
            tagById("lastcommand") {
                assertTextContains("Text:, Szam: 0")
                assertTextNotContains("Text:Felhasználónév, Szam: 12")
            }
            buttonById("mentesgomb") { click() }
            tagById("lastcommand") {
                assertTextContains("Text:Felhasználónév, Szam: 12")
            }
        }
    }

    @Test
    @DisplayName("Teszt form hibátlan lefutás")
    internal fun tesztPageObject() {
        val page1 = TesztFormPage(seleniumDriverService)
        assertThat(page1.lastCommandText).isEqualTo("Text:, Szam: 0")
        page1.sendForm(FormCommand("Felhasználónév", 12))
        assertThat(page1.lastCommandText).isEqualTo("Text:Felhasználónév, Szam: 12")
    }

    @Test
    @DisplayName("Teszt form validációs hibák")
    internal fun tesztPageObject_validationErrors() {
        val page1 = TesztFormPage(seleniumDriverService)
        assertThat(page1.lastCommandText).isEqualTo("Text:, Szam: 0")
        page1.sendForm(FormCommand("", 0))
        assertThat(page1.lastCommandText).isEqualTo("Text:, Szam: 0")
        assertThat(page1.felhasznalonevHibaUzenet).isEqualTo("Az érték nem lehet üres!")
        assertThat(page1.szamHibaUzenet).isEqualTo("Az érték legyen pozitív!")
    }

    inner class TesztFormPage(private val driver: SeleniumDriverService) {
        fun sendForm(command: FormCommand) {
            felhasznalonev.sendKeys(command.text)
            szam.clear()
            szam.sendKeys(command.szam.toString())
            mentesgomb.click()
        }

        init {
            seleniumDriverService.get("tesztform")
            if (!cimsor.text.equals("Teszt form")) {
                throw Exception("Teszt form betöltés sikertelen")
            }
        }

        val szamHibaUzenet: String get() = szamError.text
        val felhasznalonevHibaUzenet: String get() = felhasznalonevError.text
        val lastCommandText: String get() = lastcommand.text
        private val cimsor get() = driver.elementById("cimsor")
        private val felhasznalonev get() = driver.elementById("tesztform.felhasznalonev")
        private val felhasznalonevError get() = driver.elementById("tesztform.felhasznalonev.error")
        private val szam get() = driver.elementById("tesztform.szam")
        private val szamError get() = driver.elementById("tesztform.szam.error")
        private val mentesgomb get() = driver.elementById("mentesgomb")
        private val lastcommand get() = driver.elementById("lastcommand")

    }

}