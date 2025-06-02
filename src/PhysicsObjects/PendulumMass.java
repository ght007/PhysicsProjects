package PhysicsObjects;

import java.util.Vector;

/**
 * A circular mass attached to an anchor point with a rod of length l
 */
public class PendulumMass {
    public final double mass;

    public final double r;

    public final double l;

    public double x, y;

    public double theta;

    public PendulumMass(double mass, double x, double y, double l, double theta, double r) {
        this.mass = mass;
        this.theta = theta;
        this.x = x;
        this.y = y;
        this.l = l;
        this.r = r;
    }

    public double getNonCenteredX() {
        return x - r;
    }

    public double getNonCenteredY() {
        return y - r;
    }

    public void update(Vector<Double> v) {
        this.theta = v.get(0);
        this.x = v.get(1);
        this.y = v.get(2);
    }
}
