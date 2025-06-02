package PhysicsObjects;

/**
 * A rectangular Block with mass
 */
public class Block {

    public final double mass;

    public double x, y, width, height;

    public Block(double mass, double x, double y, double width, double height) {
        this.mass = mass;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

}
