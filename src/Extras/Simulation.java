package Extras;

import javax.swing.*;
import java.util.Vector;

public abstract class Simulation extends JPanel {

    protected int simulationSpeed = 1;

    protected double timestep = 0.001;

    protected double t = 0;

    protected Vector<Double> y_n = new Vector<>();

    public abstract void updatePhysics();

    public abstract double getTotalEnergy();

    protected abstract Vector<Double> f_prime(Vector<Double> f);

    public int getSimulationSpeed() {
        return simulationSpeed;
    }

    public void setSimulationSpeed(int simulationSpeed) {
        this.simulationSpeed = simulationSpeed;
    }

    public double getTimestep() {
        return timestep;
    }

    public void setTimestep(int timestep) {
        this.timestep = timestep;
    }

    public Vector<Double> getCurrentODESolution() {
        return y_n;
    }

    public void getCurrentODESolution(Vector<Double> y_n) {
        this.y_n = y_n;
    }

}
