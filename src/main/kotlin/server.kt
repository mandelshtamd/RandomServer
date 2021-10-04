package com.randomserver

import io.ktor.application.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.File
import kotlin.random.Random

fun instructions() = "Hello! This is an application to get a random string or random number\n" +
        "Please, go to /number?leftBound={value}&rightBound={value} to get a random value in [leftBound, rightBound] range.\n" +
        "Also, you can get a random string of a specific length by going to /string?size={value}\n"

fun main(args: Array<String>) {
    val portNumber = if (args.isNotEmpty()) args.first().toIntOrNull() else 8080

    require(portNumber != null) { "The passed argument is not a number. You passed '${args.first()}'" }

    embeddedServer(Netty, port = portNumber, host = "127.0.0.1") {
        routing {
            get("/") {
                call.respondText(instructions())
            }

            get("/number") {
                val leftBound = call.request.queryParameters["leftBound"]?.toInt()
                val rightBound = call.request.queryParameters["rightBound"]?.toInt()

                if (leftBound != null && rightBound != null) {
                    val number = Random.nextInt(leftBound, rightBound + 1)
                    call.respondText(number.toString())
                    ResponseLogger().logger.info("Number : $number")
                } else {
                    call.respondText("You didn't passed a number to leftBound or rightBound parameter.")
                }
            }

            get("/string") {
                val length = call.request.queryParameters["size"]?.toInt()
                val charPool = ('a'..'z') + ('A'..'Z') + ('0'..'9')

                if (length != null) {
                    val randomString = (1..length)
                        .map { _ -> Random.nextInt(0, charPool.size) }
                        .map(charPool::get)
                        .joinToString("")
                    call.respondText(randomString)
                    ResponseLogger().logger.info("String : $randomString")
                } else {
                    call.respondText("You didn't passed a number to 'size' parameter")
                }
            }

            get("/history") {
                call.respondText(File("generatedData.log").readText())
            }
        }
    }.start(wait = true)
}