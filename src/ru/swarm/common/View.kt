package ru.swarm.common

import java.awt.image.BufferedImage
import java.rmi.server.RemoteObject
import javax.swing.ImageIcon

class View(var xMousePos: Int,
           var yMousePos: Int,
           var image: ImageIcon) : java.io.Serializable {
    override fun toString(): String {
        return "View(xMousePos=$xMousePos, yMousePos=$yMousePos, image=$image)"
    }
}