package Pendulum;

import Extras.Simulation;
import PhysicsObjects.PendulumMass;

import java.awt.*;
import java.util.Vector;

import static Extras.Physics.g;
import static Solver.ODESolvers.RK4;

public class SpringPendulum extends Simulation {

    private double springConstant = 0.3;

    private PendulumMass m;

    private final double L0;

    public static SpringPendulum createDefault() {
        return new SpringPendulum(1, 150, 0, 100, Math.PI / 2, 0);
    }

    public SpringPendulum(double mass, double l, double ldot, double L0, double theta, double thetadot) {
        this.L0 = L0;
        m = new PendulumMass(mass, l * Math.sin(theta), l * -Math.cos(theta), l, theta, 10);

        y_n.add(theta);
        y_n.add(thetadot);
        y_n.add(l);
        y_n.add(ldot);
    }

    @Override
    public void updatePhysics() {
        for(int i = 0; i <= simulationSpeed; i++) {
            y_n = RK4(timestep, y_n, this::f_prime);
            updatePosition();
        }
        t += timestep;
    }

    private void updatePosition() {
        Vector<Double> mValues = new Vector<>();
        double theta = y_n.get(0);
        double r = y_n.get(2);

        mValues.add(theta);
        mValues.add(r * Math.sin(y_n.getFirst()));
        mValues.add(r * Math.cos(y_n.getFirst()));
        m.update(mValues);
        m.l = r;
    }

    @Override
    public double getTotalEnergy() {
        return 0; // TODO
    }

    @Override
    protected Vector<Double> f_prime(Vector<Double> f) {
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
        return (-2 * f.get(3) * f.get(1) - g * Math.sin(f.getFirst())) / f.get(2);
    }

    private double y4_prime(Vector<Double> f) {
        return f.get(1) * f.get(1) * f.get(2) + g * Math.cos(f.getFirst()) + (springConstant / m.mass) * (L0 - f.get(2));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int centerX = Extras.Window.WIDTH / 2, centerY = Extras.Window.HEIGHT / 2 - 100;
        g.translate(centerX, centerY);
        g.drawLine(0, 0, (int) m.x, (int) m.y);
        g.fillOval((int) m.getNonCenteredX(), (int) m.getNonCenteredY(), (int) m.r * 2, (int) m.r * 2);
    }

    public double getSpringConstant() {
        return springConstant;
    }

    public SpringPendulum setSpringConstant(double springConstant) {
        this.springConstant = springConstant;
        return this;
    }
}
