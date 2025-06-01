package SlidingBlock;

import PhysicsObjects.Block;

import javax.swing.*;
import java.awt.*;
import java.util.Vector;

import static Extras.VectorExtras.addVectors;
import static Extras.VectorExtras.scalarMultiply;

public class BlockOnInclinedPlane extends JPanel {

    private static final double g = 9.81;

    private static final double h = 0.00001;

    private static final int SPEED = 1;

    private static final double frictionCoefficient = 0.05;

    private double t = 0;

    private double theta;

    private Vector<Double> y_n = new Vector<>();

    private Block block;

    public BlockOnInclinedPlane(Block block, double theta, double x0, double xdot0) {
        this.block = block;
        this.theta = theta;

        y_n.add(x0);
        y_n.add(xdot0);
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

    public void updatePhysics() {
        for(int i = 0; i <= SPEED; i++) {
            y_n = RK4(t, y_n);
            System.out.println(y_n.getLast());
            block.x = y_n.getFirst();
        }
        t += h;
    }
    private double y2_prime(Vector<Double> f) {
        double velocity = f.get(1);
        double sinTheta = Math.sin(theta);
        double cosTheta = Math.cos(theta);

        double gravity = g * sinTheta;
        double friction = frictionCoefficient * g * cosTheta;
        double netForce = gravity;
        if (Math.abs(velocity) < 1e-6) {
            if (Math.abs(netForce) <= friction) {
                return 0;
            } else {
                return netForce - Math.signum(netForce) * friction;
            }
        }
        return netForce - Math.signum(velocity) * friction;
    }


    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        Graphics2D g = (Graphics2D) graphics;
        g.rotate(this.theta);
        g.fillRect((int) block.x, (int) block.y, (int) block.width, (int) block.height);
        g.drawLine((int) block.x - 100, (int) (block.y + block.height), (int) block.x + 100, (int) (block.y + block.height));
    }
}
