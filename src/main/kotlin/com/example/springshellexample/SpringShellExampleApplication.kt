package com.example.springshellexample

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.builder.SpringApplicationBuilder


@SpringBootApplication
class SpringShellExampleApplication

fun main(args: Array<String>) {
    SpringApplicationBuilder(SpringShellExampleApplication::class.java)
        .run(*args)
}
