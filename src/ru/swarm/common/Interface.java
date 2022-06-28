package ru.swarm.common;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Interface extends Remote{
        View get() throws RemoteException;
        void act(Action action) throws RemoteException;
}
