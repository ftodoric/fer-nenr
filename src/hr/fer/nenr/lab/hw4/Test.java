package hr.fer.nenr.lab.hw4;

public class Test {
    public static void main(String[] args) {
        BitVector solution = new BitVector(1, 1, 0, 1, 0, 0, 0, 1, 1, 0, 1, 0, 0, 0, 0, 0);
        BitVectorDecoder decoder = new BitVectorDecoder(0.01, 4);
        double[] result = decoder.decode(solution);
        System.out.println("RESULT:");
        for (double d : result) {
            System.out.println(d);
        }
    }
}
