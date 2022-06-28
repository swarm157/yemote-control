package ru.swarm.client

import ru.swarm.Client
import ru.swarm.common.Interface
import java.awt.Color
import java.awt.GridLayout
import java.awt.event.ActionEvent
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import java.rmi.Naming
import javax.swing.AbstractAction
import javax.swing.JButton
import javax.swing.JLabel
import javax.swing.JPanel

class Element(IPAAddress: String, nameA: String, portA: String, val client: Client) : JPanel() {
    var status = JLabel("please wait...")
    var selected =false
    var ip = JLabel(IPAAddress)
    var port = JLabel(portA)
    var name = JLabel(nameA)
    private val connect = JButton("connect")
    init {
        layout = GridLayout(3, 2, 5, 5)
        //setSize(165, 110)
        background = Color.white
        add(name)
        add(status)
        add(ip)
        add(port)
        add(connect)
        connect.addActionListener(Connection(this))
        addMouseListener(Mouse(this))
    }
    class Mouse(val element: Element) : MouseAdapter() {
        override fun mouseClicked(e: MouseEvent?) {
            element.apply {
                client.apply {
                    for (i in elements) {
                        i.background = Color.white
                        i.selected=false
                    }
                }
                selected=true
                background = Color.cyan
            }
        }

    }
    class Connection(val element: Element) : AbstractAction() {
        override fun actionPerformed(e: ActionEvent?) {
            element.apply {
            status.text = "connecting"
                client.apply {
                    val address = "rmi://"+element.ip.text + ":" + element.port.text+"/"+element.name.text
                    logger.info("connecting to the $address please wait")
                    api = Naming.lookup(address) as Interface
                    connected = true
                }
            }
        }

    }

    fun toElementData() : ElementData {
        return ElementData(name.text, ip.text, port.text)
    }
    
    fun fromElementData(elementData: ElementData) : Element {
        return Element(elementData.ip, elementData.name, elementData.port, client)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Element

        if (client != other.client) return false
        if (status != other.status) return false
        if (selected != other.selected) return false
        if (ip != other.ip) return false
        if (port != other.port) return false
        if (name != other.name) return false
        if (connect != other.connect) return false

        return true
    }

    override fun hashCode(): Int {
        var result = client.hashCode()
        result = 31 * result + status.hashCode()
        result = 31 * result + selected.hashCode()
        result = 31 * result + ip.hashCode()
        result = 31 * result + port.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + connect.hashCode()
        return result
    }
}