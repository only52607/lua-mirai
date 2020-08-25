package com.ooooonly.luaMirai.lua

import kotlinx.coroutines.CompletableJob

interface EventListenerContainable {
    fun addListener(job: CompletableJob)
    fun clearListeners()
}