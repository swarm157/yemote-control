package ru.swarm.experimental.api;

import ru.swarm.common.Action;
import ru.swarm.common.View;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RemoteClient extends Remote {
    View get() throws RemoteException;
    void act(Action action) throws RemoteException;
}
