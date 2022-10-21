package com.example.springshellexample.tesztform

import com.example.springshellexample.Selenium
import com.example.springshellexample.SeleniumTest
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

@SeleniumTest
internal class TesztFormControllerTest {

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
            findButtonById("mentesgomb") { click() }
            tagById("lastcommand") {
                assertTextContains("Text:Felhasználónév, Szam: 12")
            }
        }
    }
}