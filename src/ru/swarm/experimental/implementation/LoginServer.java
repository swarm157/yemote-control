package ru.swarm.experimental.implementation;

import ru.swarm.common.Interface;
import ru.swarm.experimental.api.RemoteClient;
import ru.swarm.experimental.api.RemoteConnection;
import ru.swarm.experimental.api.RemoteLogin;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LoginServer implements RemoteLogin {


    static Logger logger = Logger.getLogger(LoginServer.class.getName());
    static {
        logger.setLevel(Level.ALL);
    }
    static Map<String, RemoteClient> clients = new HashMap<>();

    @Override
    public RemoteConnection login(String name, RemoteClient client) throws RemoteException {
        Connection connection = new Connection(name, client);
        clients.put(name, client);
        logger.info(name+"logged in");
        return connection;
    }
    public static void main(String[] args) {
        try {
            Registry registry = LocateRegistry.createRegistry(Registry.REGISTRY_PORT);
            LoginServer server = new LoginServer();
            UnicastRemoteObject.exportObject(server, Registry.REGISTRY_PORT);
            registry.rebind(LoginServer.class.getName(), server);
        } catch (RemoteException e) {
            logger.severe(e.getMessage());
        }
    }
}
