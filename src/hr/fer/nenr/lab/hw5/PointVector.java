package hr.fer.nenr.lab.hw5;

public class PointVector {
    private Point[] points;

    public PointVector(Point... points) {
        this.points = new Point[points.length];
        this.points = points;
    }

    public void add(Point point) {
        Point[] newPoints = new Point[this.points.length + 1];
        System.arraycopy(this.points, 0, newPoints, 0, this.points.length);
        newPoints[newPoints.length - 1] = point;
        points = newPoints;
    }

    // GETTERS & SETTERS
    public Point at(int index) {
        return this.points[index];
    }

    public void set(int index, Point p) {
        this.points[index] = p.copy();
    }

    public int length() {
        return this.points.length;
    }

    // STATIC METHODS
    public static Point findClosestDistanceFromFirst(PointVector points, double distance, double pathPercentage) {
        if (distance == 0) return points.at(0).copy();

        double tempDistance, bestDifference = Math.abs(distance - PointVector.distanceFromFirst(points, 1));
        Point closest = points.at(1).copy();
        for (int i = 2; i < points.length(); i++) {
            if (((double) i / (double) points.length()) > pathPercentage) break;
            tempDistance = PointVector.distanceFromFirst(points, i);
            if (Math.abs(distance - tempDistance) < bestDifference) {
                bestDifference = Math.abs(distance - tempDistance);
                closest = points.at(i).copy();
            }
        }
        return closest;
    }

    public static double distanceFromFirst(PointVector points, int index) {
        double distance = 0.0;
        for (int i = 0; i < index; i++) {
            distance += Point.distance(points.at(i), points.at(i + 1));
        }
        return distance;
    }

    // UTILITY METHODS
    public void print(String heading) {
        StringBuilder sb = new StringBuilder(heading);
        sb.append(" = [");
        for (int i = 0; i < this.points.length; i++) {
            sb.append(points[i]);
            if (i != this.points.length - 1) sb.append(", ");
        }
        System.out.println(sb.append("]").toString());
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < this.points.length; i++) {
            sb.append(points[i]);
            if (i != this.points.length - 1) sb.append(" ");
        }
        return sb.toString();
    }
}
