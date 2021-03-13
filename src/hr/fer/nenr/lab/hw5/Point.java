package hr.fer.nenr.lab.hw5;

public class Point {
    @SuppressWarnings("FieldMayBeFinal")
    private double x, y;

    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return this.x;
    }

    public double getY() {
        return this.y;
    }

    // OPERATIONS
    public Point add(Point p) {
        return new Point(this.x + p.getX(), this.y + p.getY());
    }

    public Point scale(double value) {
        return new Point(this.x * value, this.y * value);
    }

    // STATIC METHODS
    public static double distance(Point p1, Point p2) {
        return Math.sqrt(Math.pow(p1.getX() - p2.getX(), 2) + Math.pow(p1.getY() - p2.getY(), 2));
    }

    // UTILITY METHODS
    public Point copy() {
        double x = this.x;
        double y = this.y;
        return new Point(x, y);
    }

    @Override
    public String toString() {
        return x + " " + y;
    }
}
