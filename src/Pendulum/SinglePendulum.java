package Pendulum;

import Extras.Simulation;
import PhysicsObjects.PendulumMass;

import java.awt.*;
import java.util.Vector;

import static Extras.Physics.g;
import static Solver.ODESolvers.RK4;

public class SinglePendulum extends Simulation {

    private double frictionCoefficient = 0;

    private PendulumMass m;

    /**
     * Creates a SinglePendulum object with default initial values for all parameters.
     * @apiNote Mass and rod length is set to {@code 100}, the angle to {@code Math.PI / 2}, the angular velocity to {@code 0} .
     * @return
     */
    public static SinglePendulum createDefault() {
        return new SinglePendulum(100, 100, Math.PI / 2, 0);
    }

    public SinglePendulum(double mass, double l, double theta, double thetadot) {
        m = new PendulumMass(mass, l * Math.sin(theta), l * Math.cos(theta), l, theta, 10);
        y_n.add(theta);
        y_n.add(thetadot);
    }

    @Override
    protected Vector<Double> f_prime(Vector<Double> f) {
        Vector<Double> functions = new Vector<>();
        double y2 = f.get(1);
        functions.add(y2);
        functions.add(y2_prime(f));
        return functions;
    }

    private double y2_prime(Vector<Double> f) {
        return (-g / m.l) * Math.sin(f.getFirst()) - (frictionCoefficient / (m.mass * m.l * m.l)) * f.getLast();
    }

    @Override
    public void updatePhysics() {
        for(int i = 0; i <= simulationSpeed; i++) {
            y_n = RK4(timestep, y_n, this::f_prime);
            updatePositions(y_n.getFirst());
        }
        t += timestep;
    }

    private void updatePositions(double theta) {
        Vector<Double> mValues = new Vector<>();
        mValues.add(theta);
        mValues.add(m.l * Math.sin(theta));
        mValues.add(m.l * Math.cos(theta));
        m.update(mValues);
    }

    @Override
    public double getTotalEnergy() {
        double potentialEnergy = -m.l * g * Math.cos(y_n.getFirst());
        double kineticEnergy = 1 / 2f * m.mass * (y_n.getLast() * y_n.getLast() * m.l * m.l);
        return potentialEnergy + kineticEnergy;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int centerX = Extras.Window.WIDTH / 2, centerY = Extras.Window.HEIGHT / 2 - 100;
        g.translate(centerX, centerY);
        g.drawLine(0, 0, (int) m.x, (int) m.y);
        g.fillOval((int) m.getNonCenteredX(), (int) m.getNonCenteredY(), (int) m.r * 2, (int) m.r * 2);
    }

    public double getFrictionCoefficient() {
        return frictionCoefficient;
    }

    public SinglePendulum setFrictionCoefficient(double frictionCoefficient) {
        this.frictionCoefficient = frictionCoefficient;
        return this;
    }
}
