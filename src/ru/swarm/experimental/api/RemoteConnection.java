package ru.swarm.experimental.api;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RemoteConnection extends Remote {
    void logout() throws RemoteException;
    String passInt(String name, int i) throws RemoteException;
}
