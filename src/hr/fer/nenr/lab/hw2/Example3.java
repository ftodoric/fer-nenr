package hr.fer.nenr.lab.hw2;

import hr.fer.nenr.lab.hw1.*;

public class Example3 {
    public static void main(String[] args) {
        IDomain u = Domain.intRange(1, 5);  //  {1, 2, 3, 4}

        IFuzzySet r = new MutableFuzzySet(Domain.combine(u, u))
                .set(DomainElement.of(1, 1), 1)
                .set(DomainElement.of(2, 2), 1)
                .set(DomainElement.of(3, 3), 1)
                .set(DomainElement.of(4, 4), 1)
                .set(DomainElement.of(1, 2), 0.3)
                .set(DomainElement.of(2, 1), 0.3)
                .set(DomainElement.of(2, 3), 0.5)
                .set(DomainElement.of(3, 2), 0.5)
                .set(DomainElement.of(3, 4), 0.2)
                .set(DomainElement.of(4, 3), 0.2);

        IFuzzySet r2 = r;

        System.out.println("Initial relation is fuzzy relation of equivalency? " + Relations.isFuzzyEquivalence(r));
        System.out.println();

        for (int i = 1; i <= 3; i++) {
            r2 = Relations.compositionOfBinaryRelations(r2, r);

            System.out.println("Number of done compositions: " + i + ". Relation is:");

            for (DomainElement e : r2.getDomain()) {
                System.out.println("mu(" + e + ") = " + r2.getValueAt(e));
            }

            System.out.println("This relation is fuzzy relation of equivalency? " + Relations.isFuzzyEquivalence(r2));
            System.out.println();
        }
    }
}
