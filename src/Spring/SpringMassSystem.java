package Spring;

import javax.swing.*;
import java.awt.*;
import java.util.Vector;

import Extras.Window;
import PhysicsObjects.Block;

import static Extras.VectorExtras.addVectors;
import static Extras.VectorExtras.scalarMultiply;

public class SpringMassSystem extends JPanel {
    private static final int SPEED = 100;

    private static final double g = 9.81;

    private static final double h = 0.02;

    private double springConstant;

    private double frictionCoefficient;

    private Block block;

    private Vector<Double> y_n = new Vector<>();

    private double t = 0;

    private double L0;

    /**
     * @param block This blocks y-position will be overwritten to match the height of the spring
     */
    public SpringMassSystem(Block block, double L0, double springConstant, double frictionCoefficient) {
        this.block = block;
        block.y = -block.height / 2;
        this.springConstant = springConstant;
        this.frictionCoefficient = frictionCoefficient;
        this.L0 = L0;
        y_n.add(block.x);
        y_n.add(0d);
    }

    private Vector<Double> RK4(double dt, Vector<Double> y_n) {
        Vector<Double> k1 = f_prime(y_n);
        Vector<Double> k2 = f_prime(addVectors(y_n, (scalarMultiply(k1, h / 2))));
        Vector<Double> k3 = f_prime(addVectors(y_n, (scalarMultiply(k2, h / 2))));
        Vector<Double> k4 = f_prime(addVectors(y_n, (scalarMultiply(k3, h))));
        Vector<Double> sum = addVectors(addVectors(k1, scalarMultiply(k2, 2)), addVectors(scalarMultiply(k3, 2), k4));
        return addVectors(y_n, scalarMultiply(sum, h / 6));
    }

    public void updatePhysics() {
        for(int i = 0; i < SPEED; i++) {
            y_n = RK4(t, y_n);
            block.x = y_n.getFirst();
        }
        System.out.println(getTotalEnergy());
        t += h;
    }

    public double getTotalEnergy() {
        double kinetic =  1/2f * block.mass * y_n.getLast() * y_n.getLast();
        double potential = 1/2f * springConstant * Math.pow((y_n.getFirst() - L0), 2);
        return potential + kinetic;
    }

    private Vector<Double> f_prime(Vector<Double> f) {
        Vector<Double> functions = new Vector<>();
        double y2 = f.get(1);
        functions.add(y2);
        functions.add(y2_prime(f));
        return functions;
    }

    public double y2_prime(Vector<Double> f) {
        return -springConstant / block.mass * (f.getFirst() - L0) - frictionCoefficient * g * Math.signum(f.getLast());
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int centerX = Window.WIDTH / 2 - 200, centerY = Window.HEIGHT / 2;
        g.translate(centerX, centerY);
        g.drawLine(0, 0, (int) block.x, 0);
        g.fillRect((int) block.x, (int) block.y, (int) block.width, (int) block.height);
    }
}
