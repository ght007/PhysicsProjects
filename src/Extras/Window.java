package Extras;

import Pendulum.DoublePendulum;
import Pendulum.SinglePendulum;
import SlidingBlock.BlockOnInclinedPlane;
import Spring.SpringMassSystem;
import PhysicsObjects.Block;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Window {

    public static final int WIDTH = 600, HEIGHT = 400;

    public static void main(String[] args) {
        springMass();
    }

    private static void doublePendulum() {
        JFrame window = new JFrame("Extras.Window");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setBackground(Color.BLACK);
        window.setSize(WIDTH, HEIGHT);
        window.setLocation(300, 300);
        DoublePendulum pendulum1 = new DoublePendulum(1000, 1000, 100, 100, Math.PI / 2, Math.PI / 2);
        pendulum1.setFrictionCoefficient(0.00000001);
        window.add(pendulum1);
        window.setVisible(true);

        JFrame window2 = new JFrame("Extras.Window");
        window2.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window2.setBackground(Color.BLACK);
        window2.setSize(WIDTH, HEIGHT);
        window2.setLocation(1000, 300);
        DoublePendulum pendulum2 = new DoublePendulum(100, 100, 100, 100, Math.PI / 2, Math.PI / 2);
        pendulum2.setFrictionCoefficient(0);
        window2.add(pendulum2);
        window2.setVisible(true);

        final boolean[] closed = {false, false};

        window.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                closed[0] = true;
            }
        });

        window2.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                closed[1] = true;
            }
        });

        while(true) {
            if(!closed[0]) {
                pendulum1.updatePhysics();
                pendulum1.repaint();
            }
            if(!closed[1]) {
                pendulum2.updatePhysics();
                pendulum2.repaint();
            }
        }
    }

    private static void singlePendulum() {
        JFrame window = new JFrame("Extras.Window");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setBackground(Color.BLACK);
        window.setSize(WIDTH, HEIGHT);
        window.setLocation(750, 300);
        var pendulum = new SinglePendulum(100, 100, Math.PI / 2, 0);
        window.add(pendulum);
        window.setVisible(true);

        final boolean[] closed = {false};

        window.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                closed[0] = true;
            }
        });

        while(!closed[0]) {
            pendulum.updatePhysics();
            pendulum.repaint();
        }
    }

    private static void springMass() {
        Block block = new Block(100, 200, 0, 48, 30);
        SpringMassSystem springMassSystem = new SpringMassSystem(block, 100, 0.1, 0);

        JFrame window = new JFrame("Spring Mass System");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setBackground(Color.BLACK);
        window.setSize(WIDTH, HEIGHT);
        window.setLocation(750, 300);
        window.add(springMassSystem);
        window.setVisible(true);

        final boolean[] closed = {false};

        window.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                closed[0] = true;
            }
        });

        while(!closed[0]) {
            springMassSystem.updatePhysics();
            springMassSystem.repaint();
        }
    }

    private static void blockOnInclinedPlane() {
        JFrame window = new JFrame("Block on an inclined plane");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setBackground(Color.BLACK);
        window.setSize(WIDTH, HEIGHT);
        window.setLocation(750, 300);
        Block block = new Block(100, 100, 300, 48, 30);
        BlockOnInclinedPlane blockOnInclinedPlane = new BlockOnInclinedPlane(block, Math.PI / -4, block.x, 0);
        window.add(blockOnInclinedPlane);
        window.setVisible(true);

        final boolean[] closed = {false};

        window.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                closed[0] = true;
            }
        });

        while(!closed[0]) {
            blockOnInclinedPlane.updatePhysics();
            blockOnInclinedPlane.repaint();
        }
    }
}
