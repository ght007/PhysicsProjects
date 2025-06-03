package Extras;

import javax.swing.*;
import java.util.Vector;

public abstract class Simulation extends JPanel {

    /**
     * Decides how many solutions are calculated per timestep.
     */
    protected int simulationSpeed = 1;

    /**
     * Timestep used for physics simulation.
     */
    protected double timestep = 0.0001;

    /**
     * Keeps track of total time elapsed in the simulation.
     */
    protected double t = 0;

    /**
     * {@code n}-dimensional Vector consisting of solutions for the differential equation system with {@code n} unknowns.
     * Preferably sorted from the function to derivatives.
     */
    protected Vector<Double> y_n = new Vector<>();

    /**
     * Calculates the new solution for {@code y_n} by incrementing the total time {@code t} by the timestep {@code timestep}.
     */
    public abstract void updatePhysics();

    /**
     * Returns the total energy in the system.
     * Note: If friction is not involved, this should remain constant.
     */
    public abstract double getTotalEnergy();

    /**
     * A function that differentiates each component of {@code f} and returns the resulting vector.
     */
    protected abstract Vector<Double> f_prime(Vector<Double> f);

    public int getSimulationSpeed() {
        return simulationSpeed;
    }

    public Simulation setSimulationSpeed(int simulationSpeed) {
        this.simulationSpeed = simulationSpeed;
        return this;
    }

    public double getTimestep() {
        return timestep;
    }

    public Simulation setTimestep(int timestep) {
        this.timestep = timestep;
        return this;
    }

    public Vector<Double> getCurrentODESolution() {
        return y_n;
    }

    public Simulation setCurrentODESolution(Vector<Double> y_n) {
        this.y_n = y_n;
        return this;
    }
}
