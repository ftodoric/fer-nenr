package hr.fer.nenr.lab.hw3;

import hr.fer.nenr.lab.hw1.*;

public class Rule {
    private final IFuzzySet[] premises;
    private final IFuzzySet conclusion;

    public Rule(IFuzzySet[] premises, IFuzzySet conclusion) {
        this.premises = premises;
        this.conclusion = conclusion;
    }

    public IFuzzySet apply(int... inputs) {
        // y = x°R

        double membership = 1.0;

        for (int i = 0; i < this.premises.length; i++) {
            if (premises[i] != null) {
                membership = Math.min(membership, premises[i].getValueAt(DomainElement.of(inputs[i])));
            }
        }

        MutableFuzzySet A = new MutableFuzzySet(this.conclusion.getDomain());

        for (int i = 0; i < A.getDomain().getCardinality(); i++) {
            A = A.set(
                    A.getDomain().elementForIndex(i),
                    Math.min(membership, this.conclusion.getValueAt(this.conclusion.getDomain().elementForIndex(i))));
        }

        return A;
    }
}
