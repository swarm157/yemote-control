package ru.swarm.experimental.api;

import ru.swarm.common.Interface;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RemoteLogin extends Remote {
    RemoteConnection login(String name, RemoteClient client) throws RemoteException;
}
