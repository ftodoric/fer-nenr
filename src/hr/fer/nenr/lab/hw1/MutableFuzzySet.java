package hr.fer.nenr.lab.hw1;

public class MutableFuzzySet implements IFuzzySet {
    private IDomain domain;
    private double[] values;

    public MutableFuzzySet(IDomain domain) {
        this.domain = domain;
        this.values = new double[domain.getCardinality()];
        for (DomainElement e : domain) {
            this.values[domain.indexOfElement(e)] = 0;
        }
    }

    public IDomain getDomain() {
        return this.domain;
    }

    public double getValueAt(DomainElement e) {
        return this.values[this.domain.indexOfElement(e)];
    }

    public MutableFuzzySet set(DomainElement e, double value) {
        this.values[this.domain.indexOfElement(e)] = value;
        return this;
    }
}
