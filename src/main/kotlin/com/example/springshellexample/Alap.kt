package com.example.springshellexample

import jakarta.validation.constraints.Min
import jakarta.validation.constraints.PositiveOrZero
import org.springframework.shell.standard.ShellComponent
import org.springframework.shell.standard.ShellMethod
import org.springframework.shell.standard.ShellOption


@ShellComponent
class Alap {
    @ShellMethod("Két szám összeadása")
    fun osszead(egyik: Int, masik: Int) = egyik + masik

    @ShellMethod("Két vagy három szám összeadása")
    fun osszead3(
        @PositiveOrZero egyik: Int,
        @Min(10) masik: Int,
        @ShellOption(defaultValue = "100", help = "Alapból 100") harmadik: Int
    ) = egyik + masik + harmadik
}