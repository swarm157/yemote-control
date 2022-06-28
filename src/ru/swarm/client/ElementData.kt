package ru.swarm.client

class ElementData(
    var name: String,
    var ip: String,
    var port: String,
) : java.io.Serializable {
    override fun toString(): String {
        return "ElementData(name='$name', ip='$ip', port='$port')"
    }

}