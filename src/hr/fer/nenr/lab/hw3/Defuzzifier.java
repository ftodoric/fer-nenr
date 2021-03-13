package hr.fer.nenr.lab.hw3;

import hr.fer.nenr.lab.hw1.DomainElement;
import hr.fer.nenr.lab.hw1.IFuzzySet;

public interface Defuzzifier {
    public DomainElement defuzzify(IFuzzySet A);
}
