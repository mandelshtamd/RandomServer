package com.randomserver

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
import kotlinx.css.CssBuilder
import kotlin.random.Random

suspend inline fun ApplicationCall.respondCss(builder: CssBuilder.() -> Unit) {
    this.respondText(CssBuilder().apply(builder).toString(), ContentType.Text.CSS)
}

fun getRandomString(size : Int) : String {
    val charPool = ('a'..'z') + ('A'..'Z') + ('0'..'9')
    return (1..size)
        .map { _ -> Random.nextInt(0, charPool.size) }
        .map(charPool::get)
        .joinToString("")
}