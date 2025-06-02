package SlidingBlock;

import Extras.Simulation;
import PhysicsObjects.Block;

import java.awt.*;
import java.util.Vector;

import static Extras.Physics.g;
import static Solver.ODESolvers.RK4;

public class BlockOnInclinedPlane extends Simulation {

    private double frictionCoefficient = 0;

    private double theta;

    private Block block;

    /**
     * Creates a BlockOnInclinedPlane object with default initial values for all parameters.
     * TODO define default values concretely
     */
    public static BlockOnInclinedPlane createDefault() {
        Block block = new Block(100, 100, 300, 48, 30);
        return new BlockOnInclinedPlane(block, Math.PI / -4, block.x, 0);
    }

    public BlockOnInclinedPlane(Block block, double theta, double x, double xdot) {
        this.block = block;
        this.theta = theta;
        y_n.add(x);
        y_n.add(xdot);
    }

    @Override
    protected Vector<Double> f_prime(Vector<Double> f) {
        Vector<Double> functions = new Vector<>();
        double y2 = f.get(1);
        functions.add(y2);
        functions.add(y2_prime(f));
        return functions;
    }

    @Override
    public void updatePhysics() {
        for(int i = 0; i <= simulationSpeed; i++) {
            y_n = RK4(timestep, y_n, this::f_prime);
            System.out.println(y_n.getLast());
            block.x = y_n.getFirst();
        }
        t += timestep;
    }

    @Override
    public double getTotalEnergy() {
        return 0;
    }

    private double y2_prime(Vector<Double> f) {
        double velocity = f.get(1);
        double sinTheta = Math.sin(theta);
        double cosTheta = Math.cos(theta);

        double gravity = g * sinTheta;
        double friction = frictionCoefficient * g * cosTheta;
        if (Math.abs(velocity) < 1e-6) {
            if (Math.abs(gravity) <= friction) {
                return 0;
            } else {
                return gravity - Math.signum(gravity) * friction;
            }
        }
        return gravity - Math.signum(velocity) * friction;
    }


    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        Graphics2D g = (Graphics2D) graphics;
        g.rotate(this.theta);
        g.fillRect((int) block.x, (int) block.y, (int) block.width, (int) block.height);
        g.drawLine((int) block.x - 100, (int) (block.y + block.height), (int) block.x + 100, (int) (block.y + block.height));
    }

    public double getFrictionCoefficient() {
        return frictionCoefficient;
    }

    public BlockOnInclinedPlane setFrictionCoefficient(double frictionCoefficient) {
        this.frictionCoefficient = frictionCoefficient;
        return this;
    }
}
