package com.randomserver

import io.ktor.application.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import kotlin.random.Random


fun main(args: Array<String>) {
    val portNumber = if (args.isNotEmpty()) args.first().toInt() else 8080
    embeddedServer(Netty, port = portNumber, host = "127.0.0.1") {
        routing {
            get("/") {
                call.respondText("Hello World!")
            }

            get("/getnumber") {
                val leftBound = call.request.queryParameters["leftBound"]?.toInt()
                val rightBound = call.request.queryParameters["rightBound"]?.toInt()
                if (leftBound != null && rightBound != null) {
                    val number = Random.nextInt(leftBound, rightBound)
                    call.respondText(number.toString())
                }
            }

            get("/getstring/{size}") {
                val length = call.parameters["size"]?.toInt()
                val charPool : List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')

                if (length != null) {
                    val randomString = (1..length)
                        .map { _ -> Random.nextInt(0, charPool.size) }
                        .map(charPool::get)
                        .joinToString("")
                    call.respondText(randomString)
                }
            }
        }
    }.start(wait = true)
}