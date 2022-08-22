package com.example.springshellexample

import com.example.springshellexample.adat.AEntity
import com.example.springshellexample.adat.ARepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.shell.standard.ShellComponent
import org.springframework.shell.standard.ShellMethod

@ShellComponent
class Adat {

    @Autowired
    private lateinit var repository: ARepository

    @ShellMethod("Új létrehozása")
    fun letrehoz(nev: String) {
        val a = AEntity(
            id = null,
            nev = nev
        )
        repository.save(a)
    }

    @ShellMethod("Listázás")
    fun listaz() {
        repository.findAll().forEach { println(it.toString()) }
    }
}