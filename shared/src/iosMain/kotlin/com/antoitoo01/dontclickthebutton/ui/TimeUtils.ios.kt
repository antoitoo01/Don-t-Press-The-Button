package com.antoitoo01.dontclickthebutton.ui

import platform.Foundation.NSProcessInfo

actual fun currentTimeMillis(): Long =
    NSProcessInfo.processInfo.systemUptime().toLong()
