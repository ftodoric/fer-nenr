package hr.fer.nenr.lab.hw4;

public class BitVectorDecoder {
    private final double xmin = -4, xmax = 4;
    private final int M, M_tot;
    private final int variableDimension;

    // delta = linear interpolation interval
    public BitVectorDecoder(double delta, int variableDimension) {

        this.M = (int) Math.ceil(Math.log(((this.xmax - this.xmin) / delta) + 1) / Math.log(2));
        this.M_tot = (int) this.M * variableDimension;

        this.variableDimension = variableDimension;
    }

    public double[] decode(BitVector x) {
        double[] result = new double[this.variableDimension];

        double nbc;
        double doubleValue;
        int resultIndex = 0;

        for (int i = 0; i < x.getLength(); i = i + this.M) {
            nbc = 0;
            for (int j = 0; j < this.M; j++) {
                nbc += (double) x.getBit(i + j) * Math.pow(2, this.M - 1 - j);
            }
            doubleValue = (nbc / (Math.pow(2, this.M) - 1.0)) * (this.xmax - this.xmin) + this.xmin;

            result[resultIndex++] = doubleValue;
        }

        return result;
    }

    public int getM_tot() {
        return this.M_tot;
    }
}
