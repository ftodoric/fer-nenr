package hr.fer.nenr.lab.hw5;

public class Matrix {
    double[][] values;

    // CONSTRUCTORS
    public Matrix(int n, int m) {
        this.values = new double[n][m];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                this.values[i][j] = 0.0;
            }
        }
    }

    public Matrix(double[][] values) {
        this.values = values;
    }

    public Matrix(int n, int m, double value) {
        this.values = new double[n][m];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                this.values[i][j] = value;
            }
        }
    }

    // SETTERS & GETTERS
    public double get(int i, int j) {
        return this.values[i][j];
    }

    public void set(int i, int j, double value) {
        this.values[i][j] = value;
    }

    public int getRows() {
        return this.values.length;
    }

    public int getCols() {
        return this.values[0].length;
    }

    public int getSize() {
        return getRows() * getCols();
    }

    // OPERATIONS
    public Matrix scale(double value) {
        double[][] array = new double[this.getRows()][this.getCols()];
        for (int i = 0; i < this.getRows(); i++) {
            for (int j = 0; j < this.getCols(); j++) {
                array[i][j] = this.get(i, j) * value;
            }
        }
        return new Matrix(array);
    }

    public Matrix add(Matrix b) {
        double[][] array = new double[this.getRows()][this.getCols()];
        for (int i = 0; i < this.getRows(); i++) {
            for (int j = 0; j < this.getCols(); j++) {
                array[i][j] = this.get(i, j) + b.get(i, j);
            }
        }
        return new Matrix(array);
    }

    public Matrix sub(Matrix b) {
        return this.add(b.scale(-1.0));
    }

    public Matrix matrixDot(Matrix b) {
        if (this.getCols() != b.getRows()) {
            System.out.println("Matrix aren't compatible for multiplication.");
            return null;
        }

        double[][] array = new double[this.getRows()][b.getCols()];

        double sum;
        for (int i = 0; i < this.getRows(); i++) {
            for (int j = 0; j < b.getCols(); j++) {
                sum = 0.0;
                for (int k = 0; k < this.getCols(); k++) {
                    sum += this.get(i, k) * b.get(k, j);
                }
                array[i][j] = sum;
            }
        }

        return new Matrix(array);
    }

    public Matrix dot(Matrix b) {
        double[][] array = new double[this.getRows()][this.getCols()];
        for (int i = 0; i < this.getRows(); i++) {
            for (int j = 0; j < this.getCols(); j++) {
                array[i][j] = this.get(i, j) * b.get(i, j);
            }
        }
        return new Matrix(array);
    }

    public Matrix transpose() {
        double[][] array = new double[this.getCols()][this.getRows()];
        for (int i = 0; i < this.getRows(); i++) {
            for (int j = 0; j < this.getCols(); j++) {
                array[j][i] = this.get(i, j);
            }
        }
        return new Matrix(array);
    }

    // UTILITY METHODS
    public Matrix copy() {
        double[][] array = new double[this.getRows()][this.getCols()];
        for (int i = 0; i < this.getRows(); i++) {
            for (int j = 0; j < this.getCols(); j++) {
                array[i][j] = this.get(i, j);
            }
        }
        return new Matrix(array);
    }

    public void print(String heading) {
        System.out.println(heading);
        for (double[] value : this.values) {
            for (int j = 0; j < this.values[0].length; j++) {
                System.out.print(value[j] + " ");
            }
            System.out.println();
        }
    }
}
