package ru.swarm

import ru.swarm.client.Element
import ru.swarm.client.Saver
import ru.swarm.common.Action
import ru.swarm.common.Interface
import ru.swarm.common.View
import java.awt.*
import java.awt.event.*
import java.awt.image.BufferedImage
import java.rmi.Naming
import java.util.logging.Level
import java.util.logging.Logger
import javax.swing.*

class Client : JFrame() {
    val logger: Logger = Logger.getLogger(this.javaClass.name)

    var panel = RemoteDesktop(this)
    var label = JLabel("here should be image")
    var rScroll = JScrollPane(panel)
    var list = JPanel()
    var elements = ArrayList<Element>()
    var control = JPanel()
    var scrollPane = JScrollPane(list)
    var add = JButton("add")
    var edit = JButton("edit")
    var delete = JButton("delete")
    var close = JButton("close")
    var checker = Timer(2500, Checker(this))
    var connection = Timer(15, Connection(this))
    val saver = Saver(this)
    lateinit var result: View
    lateinit var api: Interface
    var connected = false


    class RemoteDesktop(val client: Client) : JPanel() {
        init {
            addMouseListener(Mouse(this))
            addKeyListener(KeyBoard(this))
            addMouseWheelListener(Wheel(this))
            addMouseMotionListener(Motion(this))

        }

        /*override fun paintComponent(g: Graphics?) {
            super.paintComponent(g)
            paint(g)
        }

        override fun paint(g: Graphics?) {
            super.paint(g)
            if (client.connected) {
                val g2 = g as Graphics2D
                client.logger.severe(client.result.image.iconWidth.toString()+" "+client.result.image.iconHeight)
                //g2.drawImage(client.result.image.image, 0, 0, null)
                g2.color = Color.WHITE
                g2.fillOval(client.result.xMousePos+5, client.result.yMousePos+5, 10, 10)
                g2.color = Color.RED
                g2.drawOval(client.result.xMousePos+5, client.result.yMousePos+5, 10, 10)
            } else {
                Thread.sleep(100)
            }
        }*/
        class KeyBoard(val remoteDesktop: RemoteDesktop) : KeyListener {
            override fun keyTyped(e: KeyEvent?) {
            }

            override fun keyPressed(e: KeyEvent?) {
                if (remoteDesktop.client.connected) {
                    remoteDesktop.client.apply {
                        api.act(Action(6, e!!.keyCode.toString()))
                    }
                    //remoteDesktop.repaint()
                }
            }

            override fun keyReleased(e: KeyEvent?) {
                if (remoteDesktop.client.connected) {
                    remoteDesktop.client.apply {
                        api.act(Action(7, e!!.keyCode.toString()))
                    }
                    //remoteDesktop.repaint()
                }
            }

        }

        class Wheel(val remoteDesktop: RemoteDesktop) : MouseWheelListener {
            override fun mouseWheelMoved(e: MouseWheelEvent?) {
                if (remoteDesktop.client.connected) {
                    remoteDesktop.client.apply {
                        api.act(Action(5, e!!.scrollAmount.toString()))
                    }
                    //remoteDesktop.repaint()
                }
            }
        }

        class Motion(val remoteDesktop: RemoteDesktop) : MouseMotionListener {
            override fun mouseDragged(e: MouseEvent?) {

            }

            override fun mouseMoved(e: MouseEvent?) {
                if (remoteDesktop.client.connected) {
                    remoteDesktop.client.apply {
                        api.act(Action(0, e!!.x.toString()+" "+e!!.y.toString()))
                    }
                    //remoteDesktop.repaint()
                }
            }

        }

        class Mouse(val remoteDesktop: RemoteDesktop) : MouseListener {
            override fun mouseClicked(e: MouseEvent?) {
            }

            override fun mousePressed(e: MouseEvent?) {
                if (remoteDesktop.client.connected) {
                    remoteDesktop.client.apply {
                        //api.act(Action(e!!.button, ""))
                        if (e!!.button==MouseEvent.BUTTON1) {
                            api.act(Action(1, ""))
                        } else if (e!!.button==MouseEvent.BUTTON2) {
                            api.act(Action(3, ""))
                        }
                    }
                    //remoteDesktop.repaint()
                }
            }

            override fun mouseReleased(e: MouseEvent?) {
                if (remoteDesktop.client.connected) {
                    remoteDesktop.client.apply {
                        //api.act(Action(, ""))
                        if (e!!.button==MouseEvent.BUTTON1) {
                            api.act(Action(2, ""))
                        } else if (e!!.button==MouseEvent.BUTTON2) {
                            api.act(Action(4, ""))
                        }
                    }
                    //remoteDesktop.repaint()
                }
            }

            override fun mouseEntered(e: MouseEvent?) {
            }

            override fun mouseExited(e: MouseEvent?) {
            }

        }
    }


    class Close(private val client: Client) : AbstractAction() {override fun actionPerformed(e: ActionEvent?) {
            client.connected = false
        }}
    class Checker(private val client: Client) : AbstractAction() {override fun actionPerformed(e: ActionEvent?) {
        if (!client.connected) {
            for (element in client.elements) {
                //if (client.connected) {
                //Thread.sleep(333)
                //}
                val address = "rmi://"+element.ip.text + ":" + element.port.text+"/"+element.name.text
                client.logger.info("checking PC $address")
                try {
                    element.apply {
                        val temp = Naming.lookup(address) as Interface
                        temp.get()
                        status.text = "available"
                        client.logger.info("PC $address is available")
                    }
                } catch (e: Exception) {
                    element.status.text = "offline"
                    client.logger.warning("PC $address is offline")
                }
            }
        }

    }}
    class Connection(private val client: Client) : AbstractAction() {override fun actionPerformed(e: ActionEvent?) {
        //while (true) {
            if (client.connected) {
                client.apply {
                    result = api.get()
                    //panel.repaint()
                    label.icon = result.image
                }
            }// else {
                //Thread.sleep(100)
           // }
        //}
    }}
    class Add(private val client: Client) : AbstractAction() {

        class Dialog(private val client: Client) : JDialog(client, "add", true) {
            private val panel = JPanel()
            private val name = JTextField(20)
            private val nameL = JLabel("name")
            private val ip = JTextField(20)
            private val ipL = JLabel("ip")
            private val port = JTextField(20)
            private val portL = JLabel("port")
            private val ok = JButton("ok")
            private val cancel = JButton("cancel")
            private val mark = this
            init {
                //layout = GridLayout(2, 4, 5, 5)
                isResizable = false
                location = Point(200, 200)
                size = Dimension(320, 150)
                add(panel)
                panel.add(name)
                panel.apply {
                    add(nameL)
                    add(ip)
                    add(ipL)
                    add(port)
                    add(portL)
                    add(ok)
                    add(cancel)
                    cancel.addActionListener {
                        mark.isVisible =false
                    }
                    ok.addActionListener {
                        val temp =Element(ip.text, mark.name.text, port.text, client)
                        client.elements.add(temp)
                        client.list.add(temp)
                        client.list.layout = GridLayout(client.elements.size+5, 1, 5, 5)
                        mark.isVisible = false
                    }
                }
                isVisible = true

            }
        }

        override fun actionPerformed(e: ActionEvent?) {
        Dialog(client)
    }}
    class Edit(private val client: Client) : AbstractAction() {

        class Dialog(private val client: Client) : JDialog(client, "edit", true) {
            private val panel = JPanel()
            private val name = JTextField(20)
            private val nameL = JLabel("name")
            private val ip = JTextField(20)
            private val ipL = JLabel("ip")
            private val port = JTextField(20)
            private val portL = JLabel("port")
            private val ok = JButton("ok")
            private val cancel = JButton("cancel")
            private val mark = this
            private var element = Element("", "", "", client)

            init {
                //layout = GridLayout(2, 4, 5, 5)
                for (i in client.elements) {
                    if (i.selected) {
                        element=i
                        break
                    }
                }
                isResizable = false
                location = Point(200, 200)
                size = Dimension(320, 150)
                add(panel)
                panel.add(name)
                panel.apply {
                    add(nameL)
                    add(ip)
                    add(ipL)
                    add(port)
                    add(portL)
                    add(ok)
                    add(cancel)
                }
                ip.text = element.ip.text
                name.text = element.name.text
                port.text = element.port.text
                cancel.addActionListener {
                    mark.isVisible =false
                }
                ok.addActionListener {
                    element.ip.text = ip.text
                    element.name.text = name.text
                    element.port.text = port.text
                    isVisible = false
                }
                isVisible = true
            }
        }

        override fun actionPerformed(e: ActionEvent?) {
        Dialog(client)
    }}
    class Delete(private val client: Client) : AbstractAction() {override fun actionPerformed(e: ActionEvent?) {
        var element = Element("", "", "", client)
        for (i in client.elements) {
            if (i.selected) {
                element=i
                break
            }
        }
        client.list.remove(element)
        client.elements.remove(element)
        element.isVisible =false
        element.isEnabled =false
        client.list.layout = GridLayout(client.elements.size+5, 1, 5, 5)
    }}

    class WS(private val client: Client) : WindowListener{
        override fun windowOpened(e: WindowEvent?) {
        }

        override fun windowClosing(e: WindowEvent?) {
            client.saver.save()
        }

        override fun windowClosed(e: WindowEvent?) {
            client.saver.save()
        }

        override fun windowIconified(e: WindowEvent?) {
            client.saver.save()
        }

        override fun windowDeiconified(e: WindowEvent?) {
            client.saver.save()
        }

        override fun windowActivated(e: WindowEvent?) {
            client.saver.save()
        }

        override fun windowDeactivated(e: WindowEvent?) {
            client.saver.save()
        }

    }

    init {
        var toolkit=Toolkit.getDefaultToolkit()
        setLocation(0, 0)
        size = toolkit.screenSize.size
        logger.level = Level.ALL
        logger.info("Starting as client")
        layout = null

        checker.start()
        connection.start()
        add(rScroll)
        //panel.size = toolkit.screenSize.size
        //panel.setLocation(0, 0)
        add(control)
        //panel.add(control)
        control.add(scrollPane)
        control.background = Color.darkGray
        control.setLocation(0, 0)
        control.size = Dimension(200, toolkit.screenSize.size.height)
        scrollPane.preferredSize = Dimension(200, toolkit.screenSize.size.height-100)
        scrollPane.maximumSize = Dimension(200, toolkit.screenSize.size.height-100)
        scrollPane.minimumSize = Dimension(200, toolkit.screenSize.size.height-100)
        scrollPane.size = Dimension(200, toolkit.screenSize.size.height-100)
        scrollPane.horizontalScrollBarPolicy = ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS
        scrollPane.verticalScrollBarPolicy = ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS
        scrollPane.background = Color.gray
        list.background = Color.lightGray
        title = "YemoteControl"
        control.add(add)
        control.add(edit)
        control.add(delete)
        control.add(close)
        add.addActionListener(Add(this))
        edit.addActionListener(Edit(this))
        delete.addActionListener(Delete(this))
        close.addActionListener(Close(this))
        //list.layout = GridLayout(256, 1, 5, 5)
        //list.setSize(200, 100000)
        /*for (i in 0..20) {
            elements.add(Element("192.168.0.$i", "PC$i", portA = "25565"))
        }
        for (element in elements) {
            list.add(element)
        }*/
        saver.load()
        addWindowListener(WS(this))
        list.layout = GridLayout(elements.size+5, 1, 5, 5)

        rScroll.setLocation(200, 0)
        rScroll.size = Dimension(toolkit.screenSize.size.width-200, toolkit.screenSize.size.height)
        rScroll.minimumSize = Dimension(toolkit.screenSize.size.width-200, toolkit.screenSize.size.height)
        rScroll.maximumSize = Dimension(toolkit.screenSize.size.width-200, toolkit.screenSize.size.height)
        rScroll.preferredSize = Dimension(toolkit.screenSize.size.width-200, toolkit.screenSize.size.height)
        rScroll.background = Color.gray
        rScroll.horizontalScrollBarPolicy = ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS
        rScroll.verticalScrollBarPolicy = ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS

        panel.background = Color.gray
        panel.add(label)
        defaultCloseOperation = JFrame.EXIT_ON_CLOSE
        isResizable = false
        isVisible = true
    }
}