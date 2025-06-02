package Spring;

import java.awt.*;
import java.util.Vector;

import Extras.Simulation;
import Extras.Window;
import PhysicsObjects.Block;

import static Extras.Phyiscs.g;
import static Solver.ODESolvers.RK4;

public class SpringMassSystem extends Simulation {

    private double springConstant;

    private double frictionCoefficient;

    private Block block;

    private final double L0;

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

    @Override
    public void updatePhysics() {
        for(int i = 0; i < simulationSpeed; i++) {
            y_n = RK4(timestep, y_n, this::f_prime);
            block.x = y_n.getFirst();
        }
        t += timestep;
    }

    @Override
    public double getTotalEnergy() {
        double kinetic = 1 / 2f * block.mass * y_n.getLast() * y_n.getLast();
        double potential = 1 / 2f * springConstant * Math.pow((y_n.getFirst() - L0), 2);
        return potential + kinetic;
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

    public double getSpringConstant() {
        return springConstant;
    }

    public void setSpringConstant(double springConstant) {
        this.springConstant = springConstant;
    }

    public double getFrictionCoefficient() {
        return frictionCoefficient;
    }

    public void setFrictionCoefficient(double frictionCoefficient) {
        this.frictionCoefficient = frictionCoefficient;
    }
}
