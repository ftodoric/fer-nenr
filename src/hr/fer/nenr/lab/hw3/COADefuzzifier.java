package hr.fer.nenr.lab.hw3;

import hr.fer.nenr.lab.hw1.*;

public class COADefuzzifier implements Defuzzifier {
    public DomainElement defuzzify(IFuzzySet A) {
        IDomain U = A.getDomain();

        double numerator = 0, denominator = 0;
        for (DomainElement e : U) {
            numerator += A.getValueAt(e) * e.getComponentValue(0);
            denominator += A.getValueAt(e);
        }

        double value = numerator / denominator;

        double diff = Math.abs(U.elementForIndex(0).getComponentValue(0) - value);
        double tempDiff;
        DomainElement result = U.elementForIndex(0);
        for (DomainElement e : U) {
            tempDiff = Math.abs(e.getComponentValue(0) - value);
            if (tempDiff <= diff) {
                diff = tempDiff;
                result = e;
            }
        }

        return result;
    }
}
