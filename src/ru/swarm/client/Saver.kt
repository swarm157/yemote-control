package ru.swarm.client

import ru.swarm.Client
import java.awt.GridLayout
import java.io.*

/*
fun Dairy(): Dairy {
    val file = File("dairy.save")
    return if (file.exists() && file.isFile) {
        val stream = FileInputStream(file)
        val objectInput = ObjectInputStream(stream)
        objectInput.readObject() as Dairy
    } else {
        ru.swarm.Dairy(ArrayList<String>())
    }
}

class Previous : AbstractAction() {
    override fun actionPerformed(e: ActionEvent?) {
        dairy.pages[page]=area.text
        if (dairy.pages.size==0) page+=1
        page-=1
        number.text = (page+1).toString()
        area.text = dairy.pages[page]
    }

}
class Next : AbstractAction() {
    override fun actionPerformed(e: ActionEvent?) {
        dairy.pages[page]=area.text
        if (page==dairy.pages.size-1) dairy.pages.add("")
        page+=1
        number.text = (page+1).toString()
        area.text = dairy.pages[page]
    }

}

class WindowEvents : WindowListener {
    override fun windowOpened(e: WindowEvent?) {
        save()
    }

    override fun windowClosing(e: WindowEvent?) {
        save()
    }

    override fun windowClosed(e: WindowEvent?) {
        save()
    }

    override fun windowIconified(e: WindowEvent?) {
        save()
    }

    override fun windowDeiconified(e: WindowEvent?) {
        save()
    }

    override fun windowActivated(e: WindowEvent?) {
        save()
    }

    override fun windowDeactivated(e: WindowEvent?) {
        save()
    }

}

fun save() {
    dairy.pages[page]=area.text
    val cleared = ArrayList<String>()
    for (page in dairy.pages) {
        if (!(page == "" || page == null || page.replace(" ", "").replace("\n", "").replace("   ", "")=="")) cleared.add(page)
    }
    val temp = Dairy(cleared)
    val file = File("dairy.save")
    val stream = FileOutputStream(file)
    val out = ObjectOutputStream(stream)
    out.writeObject(temp)
    out.flush()
    out.close()
    stream.flush()
    stream.close()
}
 */

class Saver(@Transient val client: Client = Client()) : java.io.Serializable{
    var elements = ArrayList<ElementData>()
    fun save() {
        elements = ArrayList<ElementData>()
        for (e in client.elements) {
            elements.add(e.toElementData())
        }
        val file = File("profile.save")
        val stream = FileOutputStream(file)
        val out = ObjectOutputStream(stream)
        out.writeObject(this)
        out.flush()
        out.close()
        stream.flush()
        stream.close()
    }
    fun load() {
        client.logger.info("loading data from profile.save")
        try {
            val stream = FileInputStream(File("profile.save"))
            val objectInput = ObjectInputStream(stream)
            val temp = objectInput.readObject() as Saver
            elements = temp.elements
            val elem = Element("", "", "", client)
            for (e in elements) {
                val t = elem.fromElementData(e)
                client.elements.add(t)
                client.list.add(t)
            }
            client.list.layout = GridLayout(client.elements.size+5, 1, 5, 5)
            client.logger.fine("data has been loaded")
        } catch (e: Exception) {
            client.logger.warning("could not load data from profile.save")
            File("profile.save").createNewFile()
        }


    }

    override fun toString(): String {
        return "Saver(elements=$elements)"
    }
}