package ru.swarm.experimental.implementation;

import ru.swarm.common.Action;
import ru.swarm.common.View;
import ru.swarm.experimental.api.RemoteClient;

import javax.swing.*;
import java.awt.*;
import java.awt.event.InputEvent;
import java.rmi.RemoteException;

public class Client implements RemoteClient {
    Robot robot = new Robot();
    Toolkit tk = Toolkit.getDefaultToolkit();
    String name; String target;
    public Client(String name, String target) throws AWTException {
        this.name = name;
        this.target = target;
    }

    @Override
    public View get() throws RemoteException {

        return new View(MouseInfo.getPointerInfo().getLocation().x,  MouseInfo.getPointerInfo().getLocation().y, new ImageIcon(robot.createScreenCapture(
                        new Rectangle(tk.getScreenSize().width, tk.getScreenSize().height)
                ))
        );
    }

    @Override
    public void act(Action action) throws RemoteException {
        switch (action.getType()) {
            // mouse move
            case 0: {
                var temp = action.getValue().split(" ");
                robot.mouseMove(Integer.valueOf(temp[0]), Integer.valueOf(temp[1]));
            }
            // mouse left release
            case 1: {
                robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
            }
            // mouse left press
            case 2: {
                robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
            }
            // mouse right release
            case 3: {
                robot.mouseRelease(InputEvent.BUTTON2_DOWN_MASK);
            }
            // mouse right press
            case 4: {
                robot.mousePress(InputEvent.BUTTON2_DOWN_MASK);
            }
            // mouse wheel
            case 5: {
                robot.mouseWheel(Integer.valueOf(action.getValue()));
            }
            // keyboard press
            case 6: {
                robot.keyPress(Integer.valueOf(action.getValue()));
            }
            // keyboard release
            case 7: {
                robot.keyRelease(Integer.valueOf(action.getValue()));
            }
        }
    }
}
