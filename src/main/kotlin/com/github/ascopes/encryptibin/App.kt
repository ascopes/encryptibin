package com.github.ascopes.encryptibin

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

@ConfigurationPropertiesScan
@SpringBootApplication
class App

fun main(args: Array<String>) {
    runApplication<App>(*args)
}
