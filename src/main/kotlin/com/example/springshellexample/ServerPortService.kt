package com.example.springshellexample

import org.springframework.boot.web.context.WebServerInitializedEvent
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Service


@Service
class ServerPortService {
    private var port = 0
    fun getPort(): Int = port

    @EventListener
    fun onApplicationEvent(event: WebServerInitializedEvent) {
        port = event.webServer.port
    }
}