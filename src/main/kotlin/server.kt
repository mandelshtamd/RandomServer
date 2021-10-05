package com.randomserver

import io.ktor.application.*
import io.ktor.html.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import kotlinx.css.*
import kotlinx.css.properties.LineHeight
import kotlinx.css.properties.TextDecoration
import kotlinx.css.properties.TextDecorationLine
import kotlinx.html.*
import java.io.File
import kotlin.random.Random

suspend inline fun ApplicationCall.respondCss(builder: CssBuilder.() -> Unit) {
    this.respondText(CssBuilder().apply(builder).toString(), ContentType.Text.CSS)
}



fun main(args: Array<String>) {
    val portNumber = if (args.isNotEmpty()) args.first().toIntOrNull() else 8080

    require(portNumber != null) { "The passed argument is not a number. You passed '${args.first()}'" }

    embeddedServer(Netty, port = portNumber, host = "127.0.0.1") {
        routing {
            get("/styles.css") {
                call.respondCss {
                    body {
                        backgroundColor = Color.antiqueWhite
                        margin(0.px)
                        fontFamily = "Trocchi"
                        textAlign = TextAlign.center
                    }
                    rule("h1") {
                        fontSize = LinearDimension("45")
                        color = Color("#7c795d")
                    }
                    rule("form") {
                        width = LinearDimension("400px")
                        padding = "1em"
                        border = "1px solid #CCC"
                        borderRadius = LinearDimension("1em")
                        margin = "0 auto"
                        lineHeight = LineHeight("1.5")
                    }
                    rule("label") {
                        display = Display.inlineBlock
                        width = LinearDimension("120px")
                        textAlign = TextAlign.right
                    }
                    rule(".input-form") {
                        backgroundColor = Color.lightBlue
                        padding = "4px 2px"
                        border = "solid 1px"
                        margin = "2px 0 20px 10px"
                    }
                }
            }

            get("/") {
                call.respondHtml(HttpStatusCode.OK) {
                    head {
                        title {
                            +"JetBrains Research practice"
                        }
                        link(rel = "stylesheet", href = "/styles.css", type = "text/css")
                    }
                    body {
                        h1 {
                            +"Jetbrains practice task"
                        }
                        p {
                            +"Hi! It's a test task for JetBrains practice."
                            +"To get a random number/string fill the specific form, please."
                        }

                        div("input-form") {
                            form(action = "/string", method = FormMethod.post) {
                                label {
                                    +"Size of string:"
                                }
                                input {
                                    name = "size"
                                    type = InputType.text
                                    placeholder = "size"
                                    required = true
                                }
                                br
                                button(type = ButtonType.submit) {
                                    +"Get string"
                                }
                            }
                        }

                        div("input-form") {
                            form(action = "/number", method = FormMethod.post) {
                                label {
                                    +"Left bound:"
                                }
                                input {
                                    name = "leftBound"
                                    type = InputType.text
                                    required = true
                                }
                                br
                                div("right-bound") {
                                    label {
                                        +"Right bound:"
                                    }
                                    input {
                                        name = "rightBound"
                                        type = InputType.text
                                        required = true
                                    }
                                    br
                                }
                                button(type = ButtonType.submit) {
                                    +"Get number"
                                }
                            }
                        }
                        p {
                            +"Also you can look at responses history by "
                            a(href= "/history") { +"clicking here" }
                        }
                    }
                }
            }

            post("/number") {
                val params = call.receiveParameters()
                val leftBound = params["leftBound"]?.toInt()
                val rightBound = params["rightBound"]?.toInt()

                if (leftBound != null && rightBound != null) {
                    if (leftBound > rightBound) {
                        call.respondText("Oops, left bound was bigger than right bound :(")
                    }
                    val number = Random.nextInt(leftBound, rightBound + 1)
                    call.respondText(number.toString())
                    ResponseLogger.logger.info("Number : $number")
                } else {
                    call.respondText("You didn't passed a number to leftBound or rightBound parameter.")
                }
            }

            post("/string") {
                val params = call.receiveParameters()
                val size = params["size"]?.toInt()
                val charPool = ('a'..'z') + ('A'..'Z') + ('0'..'9')

                if (size != null) {
                    val randomString = (1..size)
                        .map { _ -> Random.nextInt(0, charPool.size) }
                        .map(charPool::get)
                        .joinToString("")
                    call.respondText(randomString)
                    ResponseLogger.logger.info("String : $randomString")
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