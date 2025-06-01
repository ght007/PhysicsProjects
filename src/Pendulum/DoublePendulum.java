package Pendulum;

import PhysicsObjects.PendulumMass;

import javax.swing.*;
import java.awt.*;
import java.util.Vector;

import static Extras.VectorExtras.addVectors;
import static Extras.VectorExtras.scalarMultiply;

public class DoublePendulum extends JPanel {
    public static final double g = 9.81;

    public int SPEED = 10;

    public double h = 0.00005;

    public double frictionCoefficient = 0;

    public double mass1 = 1000, mass2 = 1000;

    private double t = 100;

    private PendulumMass m1, m2;

    private Vector<Double> y_n = new Vector<>();

    public DoublePendulum(double l1, double l2, double theta1, double theta2) {
        m1 = new PendulumMass(mass1, l1 * Math.sin(theta1), l1 * Math.cos(theta1), l1, theta1, 10);
        m2 = new PendulumMass(mass2, m1.x + l2 * Math.sin(theta2), m1.y + l2 * Math.cos(theta2), l2, theta2, 10);

        y_n.add(theta1);
        y_n.add(0d);
        y_n.add(theta2);
        y_n.add(0d);
    }

    public void updatePhysics() {
        for(int i = 0; i <= SPEED; i++) {
            y_n = RK4(t, y_n);
            updatePositions(y_n.getFirst(), y_n.get(2));
        }
        t += h;
        double totalEnergy = getTotalEnergy();

        if(totalEnergy < 50) {
            System.out.println(totalEnergy);
        }
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
        double y4 = f.get(3);

        functions.add(y2);
        functions.add(y2_prime(f));
        functions.add(y4);
        functions.add(y4_prime(f));
        return functions;
    }

    private double y2_prime(Vector<Double> f) {
        double term = -1 * (m1.mass + m2.mass) * g * Math.sin(f.getFirst()) - m2.mass * m2.l * Math.pow(f.get(3), 2) * Math.sin(f.getFirst() - f.get(2)) - (m2.mass * Math.cos(f.getFirst() - f.get(2))) * (m1.l * Math.pow(f.get(1), 2) * Math.sin(f.getFirst() - f.get(2)) - g * Math.sin(f.get(2)));
        double denominator = m1.mass * m1.l + m2.mass * m1.l - m2.mass * m1.l * Math.pow(Math.cos(f.getFirst() - f.get(2)), 2);
        return term / denominator - (frictionCoefficient) * f.get(1);
    }

    private double y4_prime(Vector<Double> f) {
        double term1 = (m1.l * f.get(1) * f.get(1) * Math.sin(f.getFirst() - f.get(2)) - g * Math.sin(f.get(2))) / m2.l;
        double term2 = Math.cos(f.getFirst() - f.get(2)) / m2.l;
        double long_term = 1 * -1 * (m1.mass + m2.mass) * g * Math.sin(f.getFirst()) - m2.mass * m2.l * Math.pow(f.get(3), 2) * Math.sin(f.getFirst() - f.get(2)) - (m2.mass * Math.cos(f.getFirst() - f.get(2))) * (m1.l * Math.pow(f.get(1), 2) * Math.sin(f.getFirst() - f.get(2)) - g * Math.sin(f.get(2)));
        double term3 = long_term / (m1.mass + m2.mass - m2.mass * Math.pow(Math.cos(f.getFirst() - f.get(2)), 2));
        return term1 - term2 * term3 - (frictionCoefficient) * f.get(3);
    }

    private double getTotalEnergy() {
        double T = 0.5 * m1.mass * m1.l * m1.l * Math.pow(y_n.get(1), 2)
            + 0.5 * m2.mass * (Math.pow(y_n.get(1) * m1.l, 2)
            + Math.pow(y_n.get(3) * m2.l, 2)
            + 2 * y_n.get(1) * y_n.get(3) * m1.l * m2.l * Math.cos(y_n.get(0) - y_n.get(2)));

        double V = -g * ((m1.mass + m2.mass) * m1.l * Math.cos(y_n.get(0)) + m2.mass * m2.l * Math.cos(y_n.get(2)));

        return T + V;
    }

    private void updatePositions(double theta1, double theta2) {
        Vector<Double> m1Values = new Vector<>();
        m1Values.add(theta1);
        m1Values.add(m1.l * Math.sin(theta1));
        m1Values.add(m1.l * Math.cos(theta1));
        Vector<Double> m2Values = new Vector<>();
        m2Values.add(theta2);
        m2Values.add(m1.x + m2.l * Math.sin(theta2));
        m2Values.add(m1.y + m2.l * Math.cos(theta2));

        m1.update(m1Values);
        m2.update(m2Values);
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int centerX = Extras.Window.WIDTH / 2, centerY = Extras.Window.HEIGHT / 2 - 100;
        g.translate(centerX, centerY);
        g.drawLine(0, 0, (int) m1.x, (int) m1.y);
        g.fillOval((int) m1.getNonCenteredX(), (int) m1.getNonCenteredY(), (int) m1.r * 2, (int) m1.r * 2);
        g.fillOval((int) m2.getNonCenteredX(), (int) m2.getNonCenteredY(), (int) m2.r * 2, (int) m2.r * 2);
        g.drawLine((int) m1.x, (int) m1.y, (int) m2.x, (int) m2.y);
    }

}
