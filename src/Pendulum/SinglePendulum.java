package Pendulum;

import Extras.VectorExtras;
import PhysicsObjects.PendulumMass;

import javax.swing.*;
import java.awt.*;
import java.util.Vector;

import static Extras.VectorExtras.addVectors;
import static Extras.VectorExtras.scalarMultiply;

public class SinglePendulum extends JPanel {
    private static final double g = 9.81;

    private static final double h = 0.000009;

    private static final int SPEED = 1;

    private static final double frictionCoefficient = 0;

    private double t = 0;

    private Vector<Double> y_n = new Vector<>();

    private PendulumMass m;

    public final ExcelWriter excelWriter;

    public SinglePendulum(double l, double theta, String path) {
        m = new PendulumMass(100, l * Math.sin(theta), l * Math.cos(theta), l, theta, 10);

        y_n.add(theta);
        y_n.add(0d);

        excelWriter = new ExcelWriter(path);
    }


    private Vector<Double> RK4(double dt, Vector<Double> y_n) {
        Vector<Double> k1 = f_prime(y_n);
        Vector<Double> k2 = f_prime(addVectors(y_n, (scalarMultiply(k1, h / 2))));
        Vector<Double> k3 = f_prime(addVectors(y_n, (scalarMultiply(k2, h / 2))));
        Vector<Double> k4 = f_prime(addVectors(y_n, (scalarMultiply(k3, h))));
        Vector<Double> sum = addVectors(addVectors(k1, scalarMultiply(k2, 2)), addVectors(scalarMultiply(k3, 2), k4));
        return addVectors(y_n, scalarMultiply(sum, h / 6));
    }

    private Vector<Double> f_prime(Vector<Double> f) {
        Vector<Double> functions = new Vector<>();
        double y2 = f.get(1);
        functions.add(y2);
        functions.add(y2_prime(f));
        return functions;
    }

    private double y2_prime(Vector<Double> f) {
        return (-g / m.l) * Math.sin(f.getFirst()) - (frictionCoefficient / (m.mass * m.l * m.l)) * f.getLast();
    }

    public void updatePhysics() {
        for(int i = 0; i <= SPEED; i++) {
            y_n = RK4(t, y_n);
            updatePositions(y_n.getFirst());
        }

        double posY = -m.l * Math.cos(y_n.getFirst());
        double potentialEnergy = -m.l * g * Math.cos(y_n.getFirst());
        double kineticEnergy = 1/2f * m.mass * (y_n.getLast() * y_n.getLast() * m.l * m.l);
        synchronized(excelWriter) {
            excelWriter.addValues(y_n.getFirst(), y_n.getLast(), potentialEnergy + kineticEnergy, t, posY);
        }
        t += h;
    }

    private void updatePositions(double theta) {
        Vector<Double> mValues = new Vector<>();
        mValues.add(theta);
        mValues.add(m.l * Math.sin(theta));
        mValues.add(m.l * Math.cos(theta));
        m.update(mValues);
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int centerX = Extras.Window.WIDTH / 2, centerY = Extras.Window.HEIGHT / 2 - 100;
        g.translate(centerX, centerY);
        g.drawLine(0, 0, (int) m.x, (int) m.y);
        g.fillOval((int) m.getNonCenteredX(), (int) m.getNonCenteredY(), (int) m.r * 2, (int) m.r * 2);
    }

}
