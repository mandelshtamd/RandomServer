package com.randomserver

import io.ktor.application.*
import io.ktor.html.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import kotlinx.css.*
import kotlinx.css.properties.LineHeight
import kotlinx.html.*
import java.io.File
import kotlin.random.Random

fun Route.styles() {
    get("/styles.css") {
        call.respondCss {
            body {
                backgroundColor = Color("#e2d1c3")
                margin(0.px)
                fontFamily = "Trocchi"
                textAlign = TextAlign.center
            }
            rule("h1") {
                fontSize = LinearDimension("45")
                color = Color("#7c795d")
                borderBottom = "1px solid black"
            }
            rule("form") {
                width = LinearDimension("300px")
                padding = "2em"
                border = "1px solid #CCC"
                borderRadius = LinearDimension("1em")
                margin = "0 auto"
                lineHeight = LineHeight("1.5")
                backgroundColor = Color("#ebedee")
                marginBottom = LinearDimension("10px")
            }
            rule("label") {
                display = Display.inlineBlock
                width = LinearDimension("120px")
                textAlign = TextAlign.right
            }
            rule("input") {
                border = "none"
                backgroundColor = Color("#ebedee")
                borderBottom = "1px solid #e0e0e0"
                outline = Outline.none
            }
            rule("button") {
                backgroundColor = Color("#008CBA")
                color = Color("white")
                border = "none"
                padding = "5px 11px"
                marginTop = LinearDimension("10px")
                display = Display.inlineBlock
                fontSize = LinearDimension("16px")
                borderRadius = LinearDimension("8px")
            }
        }
    }
}


fun Route.generalPage() {
    get("/") {
        call.respondHtml(HttpStatusCode.OK) {
            head {
                title {
                    +"Test task"
                }
                link(rel = "stylesheet", href = "/styles.css", type = "text/css")
            }
            body {
                h1 {
                    +"Jetbrains test task"
                }

                p {
                    +"It's a test task for JetBrains practice. "
                    +"To get a random number/string fill the specific form, please."
                }

                div("input-form") {
                    form(action = "/string", method = FormMethod.post) {
                        label {
                            +"Size of string:"
                        }
                        input {
                            autoComplete = false
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
                            placeholder = "write lower bound"
                            type = InputType.text
                            required = true
                            max = "2147483646"
                            autoComplete = false
                        }

                        br

                        label {
                            +"Right bound:"
                        }

                        input {
                            name = "rightBound"
                            placeholder = "write upper bound"
                            type = InputType.text
                            required = true
                            max = "2147483646"
                            autoComplete = false
                        }

                        br

                        button(type = ButtonType.submit) {
                            +"Get number"
                        }
                    }
                }
                p {
                    +"Also you can look at Response history by "
                    a(href= "/history") { +"clicking here" }
                }
            }
        }
    }
}


fun Route.numberPage() {
    post("/number") {
        val params = call.receiveParameters()
        val leftBound = params["leftBound"]?.toInt()
        val rightBound = params["rightBound"]?.toInt()

        if (leftBound != null && rightBound != null) {
            if (leftBound > rightBound) call.respondText("Oops, left bound was bigger than right bound :(")

            val number = Random.nextInt(leftBound, rightBound + 1)

            call.respondText(number.toString())
            ResponseLogger.logger.info("Number : $number")
        } else {
            call.respondText("You didn't passed a number to leftBound or rightBound parameter.")
        }
    }
}


fun Route.stringPage() {
    post("/string") {
        val params = call.receiveParameters()
        val size = params["size"]?.toInt()

        if (size != null) {
            val randomString = getRandomString(size)
            call.respondText(randomString)
            ResponseLogger.logger.info("String : $randomString")
        } else {
            call.respondText("You didn't passed a number to 'size' parameter")
        }
    }
}


fun Route.historyPage() {
    get("/history") {
        call.respondText(File("generatedData.log").readText())
    }
}