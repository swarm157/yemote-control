package ru.swarm.server

import ru.swarm.common.Action
import ru.swarm.common.Interface
import ru.swarm.common.View
import java.awt.MouseInfo
import java.awt.Rectangle
import java.awt.Robot
import java.awt.Toolkit
import java.awt.event.InputEvent
import java.rmi.server.UnicastRemoteObject
import javax.swing.ImageIcon

class API : Interface, UnicastRemoteObject() {
    val robot = Robot()
    val tk = Toolkit.getDefaultToolkit()

    override fun get() : View {

        return View(MouseInfo.getPointerInfo().location.x,  MouseInfo.getPointerInfo().location.y, ImageIcon(robot.createScreenCapture(
            Rectangle(tk.screenSize.width, tk.screenSize.height)
        ))
        )
    }

    override fun act(action: Action) {
        when(action.type) {
            // mouse move
            0 -> {
                val temp = action.value.split(' ')
                robot.mouseMove(temp[0].toInt(), temp[1].toInt())
            }
            // mouse left release
            1 -> {
                robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK)
            }
            // mouse left press
            2 -> {
                robot.mousePress(InputEvent.BUTTON1_DOWN_MASK)
            }
            // mouse right release
            3 -> {
                robot.mouseRelease(InputEvent.BUTTON2_DOWN_MASK)
            }
            // mouse right press
            4 -> {
                robot.mousePress(InputEvent.BUTTON2_DOWN_MASK)
            }
            // mouse wheel
            5 -> {
                robot.mouseWheel(action.value.toInt())
            }
            // keyboard press
            6 -> {
                robot.keyPress(action.value.toInt())
            }
            // keyboard release
            7 -> {
                robot.keyRelease(action.value.toInt())
            }
        }
    }
}