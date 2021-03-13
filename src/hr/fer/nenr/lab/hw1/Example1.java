package hr.fer.nenr.lab.hw1;

public class Example1 {
    public static void main(String[] args) {
        IDomain d1 = Domain.intRange(0, 11); //  {0, 1, ..., 10}

        IFuzzySet set1 = new MutableFuzzySet(d1)
                .set(DomainElement.of(0), 1.0)
                .set(DomainElement.of(1), 0.8)
                .set(DomainElement.of(2), 0.6)
                .set(DomainElement.of(3), 0.4)
                .set(DomainElement.of(4), 0.2);
        Debug.print(set1, "Fuzzy set1:");

        IDomain d2 = Domain.intRange(-5, 6);
        IFuzzySet set2 = new CalculatedFuzzySet(
                d2,
                StandardFuzzySets.lambdaFunction(
                        d2.indexOfElement(DomainElement.of(-4)),
                        d2.indexOfElement(DomainElement.of(0)),
                        d2.indexOfElement(DomainElement.of(4))
                )
        );
        Debug.print(set2, "Fuzzy set2 (lambda):");

        IFuzzySet set3 = new CalculatedFuzzySet(
                d2,
                StandardFuzzySets.lFunction(
                        d2.indexOfElement(DomainElement.of(-4)),
                        d2.indexOfElement(DomainElement.of(4))
                )
        );
        Debug.print(set3, "Fuzzy set3 (L):");

        IFuzzySet set4 = new CalculatedFuzzySet(
                d2,
                StandardFuzzySets.gammaFunction(
                        d2.indexOfElement(DomainElement.of(-4)),
                        d2.indexOfElement(DomainElement.of(4))
                )
        );
        Debug.print(set4, "Fuzzy set4 (gamma):");
    }
}
