package ru.swarm

import ru.swarm.experimental.implementation.LoginServer
import java.util.logging.Level
import java.util.logging.Logger
import kotlin.system.exitProcess

var drawGUI = true



fun main(args: Array<String>) {
    System.setProperty("java.security.policy","file://server.policy");
    for (arg in args) {
        when(arg) {
            "--nogui" -> {
                drawGUI=false
            }
            "--help" -> {
               println("--nogui to run server only\n --help to show command list")
               exitProcess(0)
            }
            else -> {
                println("please type --help to get commands list")
                exitProcess(0)
            }
        }
    }
    if (drawGUI) {
        Client()
    } else {
        LoginServer.main(null)
        //Server().start()
    }
}