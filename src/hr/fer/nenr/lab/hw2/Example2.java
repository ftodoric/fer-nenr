package hr.fer.nenr.lab.hw2;

import hr.fer.nenr.lab.hw1.*;

public class Example2 {
    public static void main(String[] args) {
        IDomain u = Domain.intRange(1, 5); //  {1, 2, 3, 4}
        IDomain v = Domain.intRange(1, 4); //  {1, 2, 3}
        IDomain w = Domain.intRange(1, 5); //  {1, 2, 3, 4}

        IFuzzySet r1 = new MutableFuzzySet(Domain.combine(u, v))
                .set(DomainElement.of(1, 1), 0.3)
                .set(DomainElement.of(1, 2), 1)
                .set(DomainElement.of(3, 3), 0.5)
                .set(DomainElement.of(4, 3), 0.5);

        IFuzzySet r2 = new MutableFuzzySet(Domain.combine(v, w))
                .set(DomainElement.of(1, 1), 1)
                .set(DomainElement.of(2, 1), 0.5)
                .set(DomainElement.of(2, 2), 0.7)
                .set(DomainElement.of(3, 3), 1)
                .set(DomainElement.of(3, 4), 0.4);

        IFuzzySet r1r2 = Relations.compositionOfBinaryRelations(r1, r2);

        for (DomainElement e : r1r2.getDomain()) {
            System.out.println("mu(" + e + ") = " + r1r2.getValueAt(e));
        }
    }
}
