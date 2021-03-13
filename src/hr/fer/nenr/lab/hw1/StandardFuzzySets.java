package hr.fer.nenr.lab.hw1;

public class StandardFuzzySets {

    public static IIntUnaryFunction lFunction(int first, int last) {
        return new LFunction(first, last);
    }

    public static IIntUnaryFunction gammaFunction(int first, int last) {
        return new GammaFunction(first, last);
    }

    public static IIntUnaryFunction lambdaFunction(int first, int middle, int last) {
        return new LambdaFunction(first, middle, last);
    }
}
