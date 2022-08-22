package com.example.springshellexample

import org.springframework.shell.standard.ShellComponent
import org.springframework.shell.standard.ShellMethod
import org.springframework.shell.standard.commands.Quit
import kotlin.system.exitProcess


@ShellComponent
class QuitCommand : Quit.Command {
    @ShellMethod(value = "Exit the shell.", key = ["quit", "exit", "terminate"])
    fun quit() {
        println("Exiting the Application")
        println("Good Bye!!!!!!!!")
        exitProcess(0)
    }
}