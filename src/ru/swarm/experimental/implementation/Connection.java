package ru.swarm.experimental.implementation;

import ru.swarm.experimental.api.RemoteClient;
import ru.swarm.experimental.api.RemoteConnection;

import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.server.Unreferenced;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class Connection implements RemoteConnection, Unreferenced {
    static Logger logger = Logger.getLogger(Connection.class.getName());
    static {
        logger.setLevel(Level.ALL);
    }
    String name; RemoteClient client;
    public Connection(String name, RemoteClient client) {
        this.client = client;
        this.name = name;
        try {
            UnicastRemoteObject.exportObject(this, Registry.REGISTRY_PORT);
        } catch (RemoteException e) {
            logger.severe(e.getMessage());
        }
    }

    @Override
    public void logout() throws RemoteException {
        try {
            UnicastRemoteObject.unexportObject(this, true);
        } catch (Exception e) {
            logger.severe(e.getMessage());
        }
    }

    @Override
    public String passInt(String name, int i) throws RemoteException {
        logger.info("Server received from "+name+":"+i);
        if (i<10)
            return String.valueOf(i);
        RemoteClient client = LoginServer.clients.get(name);
        try {
            client.get();
        } catch (Exception e) {
            logger.severe(e.getMessage());
        }
        return String.valueOf(i);
    }

    @Override
    public void unreferenced() {
        try {
            UnicastRemoteObject.unexportObject(this, true);
        } catch (Exception e) {
            logger.severe(e.getMessage());
        }
    }
}
