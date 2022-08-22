package com.example.springshellexample.tesztform

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext
import org.springframework.shell.standard.ShellComponent
import org.springframework.shell.standard.ShellMethod
import org.springframework.stereotype.Controller
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PostMapping
import java.net.InetAddress
import javax.validation.Valid
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Positive

data class FormCommand(
    @field:NotBlank(message = "Az érték nem lehet üres!")
    var text: String,
    @field:Positive(message = "Az érték legyen pozitív!")
    var szam: Int,
)

@ShellComponent
@Controller
class TesztFormController {
    @Autowired
    private val webServerAppCtxt: ServletWebServerApplicationContext? = null

    @ShellMethod("Teszt form linkjének megjelenítése")
    fun tesztFormShell() {
        listOf(
            InetAddress.getLocalHost().hostAddress,
            InetAddress.getLocalHost().hostName,
            InetAddress.getLoopbackAddress().hostAddress,
            InetAddress.getLoopbackAddress().hostName,
        ).forEach {
            println("http://$it:${webServerAppCtxt!!.webServer.port}/tesztform")
        }
    }

    @GetMapping("/tesztform")
    fun tesztForm(): String {
        return "tesztform";
    }

    @PostMapping("/tesztform")
    fun tesztFormSend(
        @ModelAttribute("formCommand") @Valid formCommand: FormCommand,
        bindingResult: BindingResult
    ): String {
        println(formCommand)
        return "tesztform"
    }

    @ModelAttribute("formCommand")
    fun formCommand() = FormCommand("", 0)
}