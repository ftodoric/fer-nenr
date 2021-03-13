package hr.fer.nenr.lab.hw1;

import java.util.Iterator;

public class SimpleDomain extends Domain {
    private int first, last;

    public SimpleDomain(int first, int last) {
        this.first = first;
        this.last = last;
    }

    public int getCardinality() {
        return this.last - this.first;
    }

    public IDomain getComponent(int index) {
        return this;
    }

    public int getNumberOfComponents() {
        return 1;
    }

    public Iterator<DomainElement> iterator() {
        return new Iterator<DomainElement>() {
            private int currentIndex = 0;

            @Override
            public boolean hasNext() {
                return currentIndex < getCardinality();
            }

            @Override
            public DomainElement next() {
                return DomainElement.of(first + currentIndex++);
            }
        };
    }

    public int getFirst() {
        return this.first;
    }

    public int getLast() {
        return this.last;
    }
}
