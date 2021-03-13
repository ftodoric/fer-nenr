package hr.fer.nenr.lab.hw1;

public abstract class Domain implements IDomain {

    public static IDomain intRange(int first, int last) {
        return new SimpleDomain(first, last);
    }

    public static Domain combine(IDomain domain1, IDomain domain2) {
        SimpleDomain[] simples = new SimpleDomain[domain1.getNumberOfComponents() + domain2.getNumberOfComponents()];

        int k = 0;
        for (int i = 0; i < domain1.getNumberOfComponents(); i++) {
            simples[k++] = (SimpleDomain) domain1.getComponent(i);
        }
        for (int i = 0; i < domain2.getNumberOfComponents(); i++) {
            simples[k++] = (SimpleDomain) domain2.getComponent(i);
        }

        return new CompositeDomain(simples);
    }

    public int indexOfElement(DomainElement e) {
        int i = 0;
        for (DomainElement currentElement : this) {
            if (currentElement.equals(e)) return i;
            i++;
        }
        return -1;
    }

    public DomainElement elementForIndex(int index) {
        int i = 0;
        for (DomainElement currentElement : this) {
            if (i++ == index) return currentElement;
        }
        return null;
    }
}
