package com.example.springshellexample.tesztform

import com.example.springshellexample.SeleniumTest
import com.example.springshellexample.SeleniumTestBase
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@SeleniumTest
internal class TesztFormControllerTest : SeleniumTestBase() {
    @BeforeEach
    internal fun setUp() {
        get("tesztform")
    }

    @Test
    fun tesztForm() {
        findTagById("cimsor")
            .assertTextContains("Teszt form")
        findInputById("tesztform.felhasznalonev")
            .write("Felhasználónév")
        findInputById("tesztform.szam")
            .write("12")
        findTagById("lastcommand")
            .assertTextNotContains("Text:Felhasználónév, Szam: 12")

        findButtonById("mentesgomb")
            .click()

        findTagById("lastcommand")
            .assertTextContains("Text:Felhasználónév, Szam: 12")
    }

}