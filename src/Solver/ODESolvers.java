package Solver;

import java.util.Vector;
import java.util.function.Function;

import static Extras.VectorExtras.addVectors;
import static Extras.VectorExtras.scalarMultiply;

public class ODESolvers {

    public static Vector<Double> RK4(double dt, Vector<Double> y_n, Function<Vector<Double>, Vector<Double>> f_prime) {
        Vector<Double> k1 = f_prime.apply(y_n);
        Vector<Double> k2 = f_prime.apply(addVectors(y_n, (scalarMultiply(k1, dt / 2))));
        Vector<Double> k3 = f_prime.apply(addVectors(y_n, (scalarMultiply(k2, dt / 2))));
        Vector<Double> k4 = f_prime.apply(addVectors(y_n, (scalarMultiply(k3, dt))));
        Vector<Double> sum = addVectors(addVectors(k1, scalarMultiply(k2, 2)), addVectors(scalarMultiply(k3, 2), k4));
        return addVectors(y_n, scalarMultiply(sum, dt / 6));
    }
}
