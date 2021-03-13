package hr.fer.nenr.lab.hw1;

public class CalculatedFuzzySet implements IFuzzySet {
    private IDomain domain;
    private IIntUnaryFunction func;

    public CalculatedFuzzySet(IDomain domain, IIntUnaryFunction func) {
        this.domain = domain;
        this.func = func;
    }

    public IDomain getDomain() {
        return this.domain;
    }

    public double getValueAt(DomainElement e) {
        return this.func.valueAt(this.domain.indexOfElement(e));
    }
}
