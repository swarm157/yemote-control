package ru.swarm.common

class Action(var type: Int, var value: String) : java.io.Serializable {
    override fun toString(): String {
        return "Action(type=$type, value=$value)"
    }
}