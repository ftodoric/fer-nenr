package hr.fer.nenr.lab.hw1;

public interface IDomain extends Iterable<DomainElement> {
    public int getCardinality();

    public IDomain getComponent(int index);

    public int getNumberOfComponents();

    public int indexOfElement(DomainElement element);

    public DomainElement elementForIndex(int index);
}
