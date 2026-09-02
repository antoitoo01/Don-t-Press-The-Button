package com.antoitoo01.dontclickthebutton

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform