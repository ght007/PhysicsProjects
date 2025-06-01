package Extras;

import java.util.Vector;

public class VectorExtras {
    public static Vector<Double> addVectors(Vector<Double> a, Vector<Double> b) {
        if(a.size() != b.size()) {
            throw new IllegalArgumentException("Vectors must be the same length");
        }

        Vector<Double> result = new Vector<>(a.size());
        for(int i = 0; i < a.size(); i++) {
            result.add(a.get(i) + b.get(i));
        }
        return result;
    }

    public static Vector<Double> scalarMultiply(Vector<Double> vector, double scalar) {
        Vector<Double> result = new Vector<>(vector.size());
        for(Double value : vector) {
            result.add(value * scalar);
        }
        return result;
    }
}
