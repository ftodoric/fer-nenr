package hr.fer.nenr.lab.hw2;

import hr.fer.nenr.lab.hw1.*;

public class Example1 {
    public static void main(String[] args) {
        IDomain u = Domain.intRange(1, 6);  //  {1, 2, 3, 4, 5}

        IDomain u2 = Domain.combine(u, u);
        IFuzzySet r1 = new MutableFuzzySet(u2)
                .set(DomainElement.of(1, 1), 1)
                .set(DomainElement.of(2, 2), 1)
                .set(DomainElement.of(3, 3), 1)
                .set(DomainElement.of(4, 4), 1)
                .set(DomainElement.of(5, 5), 1)
                .set(DomainElement.of(3, 1), 0.5)
                .set(DomainElement.of(1, 3), 0.5);

        IFuzzySet r2 = new MutableFuzzySet(u2)
                .set(DomainElement.of(1, 1), 1)
                .set(DomainElement.of(2, 2), 1)
                .set(DomainElement.of(3, 3), 1)
                .set(DomainElement.of(4, 4), 1)
                .set(DomainElement.of(5, 5), 1)
                .set(DomainElement.of(3, 1), 0.5)
                .set(DomainElement.of(1, 3), 0.1);

        IFuzzySet r3 = new MutableFuzzySet(u2)
                .set(DomainElement.of(1, 1), 1)
                .set(DomainElement.of(2, 2), 1)
                .set(DomainElement.of(3, 3), 0.3)
                .set(DomainElement.of(4, 4), 1)
                .set(DomainElement.of(5, 5), 1)
                .set(DomainElement.of(1, 2), 0.6)
                .set(DomainElement.of(2, 1), 0.6)
                .set(DomainElement.of(2, 3), 0.7)
                .set(DomainElement.of(3, 2), 0.7)
                .set(DomainElement.of(3, 1), 0.5)
                .set(DomainElement.of(1, 3), 0.5);

        IFuzzySet r4 = new MutableFuzzySet(u2)
                .set(DomainElement.of(1, 1), 1)
                .set(DomainElement.of(2, 2), 1)
                .set(DomainElement.of(3, 3), 1)
                .set(DomainElement.of(4, 4), 1)
                .set(DomainElement.of(5, 5), 1)
                .set(DomainElement.of(1, 2), 0.4)
                .set(DomainElement.of(2, 1), 0.4)
                .set(DomainElement.of(2, 3), 0.5)
                .set(DomainElement.of(3, 2), 0.5)
                .set(DomainElement.of(1, 3), 0.4)
                .set(DomainElement.of(3, 1), 0.4);

        boolean test1 = Relations.isUtimesURelation(r1);
        System.out.println("R1 is defined over UxU? " + test1);

        boolean test2 = Relations.isSymmetric(r1);
        System.out.println("R1 is symmetric? " + test2);

        boolean test3 = Relations.isSymmetric(r2);
        System.out.println("R2 is symmetric? " + test3);

        boolean test4 = Relations.isReflexive(r1);
        System.out.println("R1 is reflexive? " + test4);

        boolean test5 = Relations.isReflexive(r3);
        System.out.println("R3 is reflexive? " + test5);

        boolean test6 = Relations.isMaxMinTransitive(r3);
        System.out.println("R3 is max-min transitive? " + test6);

        boolean test7 = Relations.isMaxMinTransitive(r4);
        System.out.println("R4 is max-min transitive? " + test7);
    }
}
