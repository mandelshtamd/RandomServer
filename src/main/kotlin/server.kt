package com.randomserver

import io.ktor.routing.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*


fun main(args: Array<String>) {
    val portNumber = if (args.isNotEmpty()) args.first().toIntOrNull() else 8080

    require(portNumber != null) { "The passed argument is not a number. You passed '${args.first()}'" }

    embeddedServer(Netty, port = portNumber, host = "127.0.0.1") {
        routing {
            styles()
            generalPage()
            numberPage()
            stringPage()
            historyPage()
        }
    }.start(wait = true)
}