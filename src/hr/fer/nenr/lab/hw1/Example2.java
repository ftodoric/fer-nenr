package hr.fer.nenr.lab.hw1;

public class Example2 {
    public static void main(String[] args) {
        IDomain d = Domain.intRange(0, 11);

        IFuzzySet set1 = new MutableFuzzySet(d)
                .set(DomainElement.of(0), 1.0)
                .set(DomainElement.of(1), 0.8)
                .set(DomainElement.of(2), 0.6)
                .set(DomainElement.of(3), 0.4)
                .set(DomainElement.of(4), 0.2);
        Debug.print(set1, "Fuzzy set1:");

        IFuzzySet notSet1 = Operations.unaryOperation(set1, Operations.zadehNot());
        Debug.print(notSet1, "Fuzzy not(set1):");

        IFuzzySet union = Operations.binaryOperation(set1, notSet1, Operations.zadehOr());
        Debug.print(union, "Fuzzy union(set1, notSet1):");

        IFuzzySet intersection = Operations.binaryOperation(set1, notSet1, Operations.zadehAnd());
        Debug.print(intersection, "Fuzzy intersection(set1, notSet1):");

        IFuzzySet hinters = Operations.binaryOperation(set1, notSet1, Operations.hamacherTNorm(1.0));
        Debug.print(hinters, "Set1 intersection with notSet1 using parameterised Hamacher T norm with parameter 1.0:");
    }
}
