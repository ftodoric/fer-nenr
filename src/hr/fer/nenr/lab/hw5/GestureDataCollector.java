package hr.fer.nenr.lab.hw5;

import java.awt.event.*;
import java.io.*;
import java.nio.file.*;

public class GestureDataCollector implements MouseListener, MouseMotionListener {
    private boolean gestureCollectingPhase = true;
    private PointVector gesture;
    private final int M;
    private final Path gestureDataFilePath;
    private ArtificialNeuralNetwork ann;
    private int sampleCounter;
    @SuppressWarnings("FieldCanBeLocal")
    private final int numOfSamplesPerSymbol = 20;

    public GestureDataCollector(Path gestureDataFilePath, int M) {
        this.gestureDataFilePath = gestureDataFilePath;
        this.M = M;
    }

    public void setPredictingPhase() {
        this.gestureCollectingPhase = false;
    }

    public Path getGestureDataFilePath() {
        return this.gestureDataFilePath;
    }

    public int getM() {
        return this.M;
    }

    // MOUSE MOTION METHODS
    @Override
    public void mouseDragged(MouseEvent e) {
        this.gesture.add(new Point(e.getX(), e.getY()));
    }

    @Override
    public void mouseMoved(MouseEvent e) {
    }

    // MOUSE METHODS
    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        this.gesture = new PointVector(new Point(e.getX(), e.getY()));
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (this.gestureCollectingPhase) {
            // if collecting data and file is not empty
            if (this.sampleCounter == 0) {
                try {
                    Files.writeString(this.gestureDataFilePath, "");
                } catch (IOException exc) {
                    exc.printStackTrace();
                }
            }

            // find M representative points
            PointVector representatives = scaleData(this.gesture, GestureRecognition.height);

            StringBuilder datasetString = new StringBuilder();
            datasetString.append(representatives.toString());

            // 2 samples for each symbols, 10 in total
            if (this.sampleCounter < numOfSamplesPerSymbol)
                datasetString.append(" 1,0,0,0,0");
            else if (this.sampleCounter < numOfSamplesPerSymbol * 2)
                datasetString.append(" 0,1,0,0,0");
            else if (this.sampleCounter < numOfSamplesPerSymbol * 3)
                datasetString.append(" 0,0,1,0,0");
            else if (this.sampleCounter < numOfSamplesPerSymbol * 4)
                datasetString.append(" 0,0,0,1,0");
            else if (this.sampleCounter < numOfSamplesPerSymbol * 5)
                datasetString.append(" 0,0,0,0,1");
            datasetString.append("\n");

            this.sampleCounter++;

            /*
              File with gesture dataset is in following form:
              ROW: x1 y1 x2 y2 x3 y3 ... xM yM <label>
              Sample consists of points. Each point consists of coordinate x and y.
              One sample is placed in one single row. Features of a sample are separated by space.
              Sample label is located at the end of a row separated by space.
              Label is in form of: <0,0,0,1,0> representing classes for each of the five symbols.
             */
            try {
                Files.writeString(this.gestureDataFilePath, datasetString.toString(), StandardOpenOption.APPEND);
            } catch (IOException exc) {
                exc.printStackTrace();
            }
        } else {
            // SCALE THE DATA
            PointVector representatives = scaleData(this.gesture, GestureRecognition.height);

            // TRANSFORM INTO MATRIX FORM
            double[][] array = new double[1][representatives.length() * 2];
            String[] stringArray = representatives.toString().split("\\s+");
            for (int j = 0; j < stringArray.length; j++) {
                array[0][j] = Double.parseDouble(stringArray[j]);
            }
            Matrix x = new Matrix(array);
            Matrix result = ann.predict(x);
            ann.setLabel(result);
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    public PointVector scaleData(PointVector gesture, int height) {
        // invert y-axis
        for (int i = 0; i < gesture.length(); i++) {
            gesture.set(i, new Point(gesture.at(i).getX(), height - gesture.at(i).getY()));
        }

        // get Tc
        Point Tc = new Point(0, 0);
        for (int i = 0; i < gesture.length(); i++) {
            Tc = Tc.add(gesture.at(i));
        }
        Tc = Tc.scale(1.0 / gesture.length());

        // subtract Tc from all points
        for (int i = 0; i < gesture.length(); i++) {
            gesture.set(i, gesture.at(i).add(Tc.scale(-1.0)));
        }

        // get max x & max y
        double max_x = Math.abs(gesture.at(0).getX());
        double max_y = Math.abs(gesture.at(0).getY());
        double x_temp, y_temp;
        for (int i = 1; i < gesture.length(); i++) {
            x_temp = Math.abs(gesture.at(i).getX());
            y_temp = Math.abs(gesture.at(i).getY());
            if (x_temp > max_x) max_x = x_temp;
            if (y_temp > max_y) max_y = y_temp;
        }
        double m = Math.max(max_x, max_y);

        // divide all points with m
        for (int i = 0; i < gesture.length(); i++) {
            gesture.set(i, gesture.at(i).scale(1.0 / m));
        }

        // get distance D
        double D = 0.0;
        for (int i = 0; i < gesture.length(); i++) {
            if (i != gesture.length() - 1) D += Point.distance(gesture.at(i), gesture.at(i + 1));
        }

        return findRepresentativePoints(D);
    }

    public PointVector findRepresentativePoints(double D) {
        Point[] representatives = new Point[this.M];
        double distance;
        Point representative;
        int i = 0;
        for (int k = 0; k <= this.M - 1; k++) {
            distance = ((double) k * D) / (this.M - 1.0);
            representative = PointVector.findClosestDistanceFromFirst(this.gesture, distance, distance / D);
            representatives[i++] = representative;
        }
        return new PointVector(representatives);
    }

    public void setANN(ArtificialNeuralNetwork ann) {
        this.ann = ann;
    }
}
