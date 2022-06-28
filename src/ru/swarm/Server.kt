package ru.swarm

import ru.swarm.server.API
import java.io.BufferedWriter
import java.io.File
import java.io.FileReader
import java.io.FileWriter
import java.rmi.Naming
import java.rmi.registry.LocateRegistry
import java.util.Properties
import java.util.logging.Level
import java.util.logging.Logger
import javax.naming.Context
import javax.naming.InitialContext

class Server : Thread() {
    override fun start() {
        super.start()
        val logger = Logger.getLogger(this.javaClass.name)
        val context = InitialContext()
        logger.level = Level.ALL
        val properties = Properties()
        if (!File("server.properties").exists()) {
            File("server.properties").createNewFile()
        }
        properties.load(FileReader(File("server.properties")))
        if (properties.size<3) {
            val file = File("server.properties")
            val bufferedWriter = BufferedWriter(FileWriter(file))
            bufferedWriter.write("ip=localhost\n")
            bufferedWriter.write("port=25565\n")
            bufferedWriter.write("name=unnamed\n")
            bufferedWriter.flush()
            bufferedWriter.close()
        }
        for (property in properties) {
            logger.info(property.key as String+"="+property.value)
        }
        properties.load(FileReader(File("server.properties")))
        val port = properties["port"] as String
        val ip = properties["ip"] as String
        val name = properties["name"] as String
        logger.info("Starting as server")
        try {
            //"rmi://"+element.ip.text + ":" + element.port.text+"/"+element.name.text
            logger.info("server address is \"rmi://$ip:$port/$name\"")
            context.rebind("rmi://$ip:$port/$name", API())
            logger.fine("Server ready")
        } catch (e: Exception) {
            logger.severe("Server exception: $e")
        }
    }
}