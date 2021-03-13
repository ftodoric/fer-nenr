package hr.fer.nenr.lab.hw4;

import java.util.Arrays;
import java.util.Objects;

public class BitVector {
    private final int N;
    private final int[] bits;
    private double fitness;

    public BitVector(int... bits) {
        this.bits = bits;
        this.N = bits.length;
    }

    public int getLength() {
        return this.N;
    }

    public int getBit(int index) {
        return this.bits[index];
    }

    public void setBit(int index) {
        this.bits[index] = 1 - this.bits[index];
    }

    public double getFitness() {
        return this.fitness;
    }

    public void setFitness(double fitness) {
        this.fitness = fitness;
    }

    // UTILITY METHODS
    public BitVector copy() {
        int[] bits = new int[this.bits.length];
        System.arraycopy(this.bits, 0, bits, 0, this.bits.length);
        BitVector result = new BitVector(bits);
        result.setFitness(this.fitness);
        return result;
    }

    public void printDecoded(BitVectorDecoder decoder) {
        double[] values = decoder.decode(this);
        StringBuilder sb = new StringBuilder();
        for (double value : values) {
            sb.append(value).append(" ");
        }
        System.out.println(sb.toString());
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < this.bits.length; i++) {
            sb.append(this.bits[i]);
        }
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BitVector bitVector = (BitVector) o;
        return N == bitVector.N &&
                Arrays.equals(bits, bitVector.bits);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(N);
        result = 31 * result + Arrays.hashCode(bits);
        return result;
    }
}
