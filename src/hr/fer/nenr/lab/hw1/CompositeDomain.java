package hr.fer.nenr.lab.hw1;

import java.util.Iterator;

public class CompositeDomain extends Domain {
    private SimpleDomain[] components;

    public CompositeDomain(SimpleDomain[] domains) {
        this.components = domains;
    }

    public Iterator<DomainElement> iterator() {
        return new CompositeIterator();
    }

    private class CompositeIterator implements Iterator<DomainElement> {
        private int counter = 0;
        int[] values = new int[getNumberOfComponents()];
        private final int[] indices = new int[components.length];

        public CompositeIterator() {
            resetIndices();
        }

        public void resetIndices() {
            for (int i = 0; i < this.indices.length; i++) {
                indices[i] = 0;
            }
        }

        @Override
        public boolean hasNext() {
            return counter < getCardinality();
        }

        @Override
        public DomainElement next() {
            for (int i = 0; i < components.length; i++) {
                values[i] = components[i].elementForIndex(indices[i]).getComponentValue(0);
            }
            // set the indices
            for (int i = indices.length - 1; i > -1; i--) {
                if (indices[i] != components[i].getCardinality() - 1) {
                    indices[i]++;
                    break;
                } else {
                    indices[i] = 0;
                }
            }
            counter++;
            return new DomainElement(values);
        }

    }

    public int getCardinality() {
        int card = 1;
        for (SimpleDomain component : this.components) {
            card *= component.getCardinality();
        }
        return card;
    }

    public IDomain getComponent(int index) {
        return this.components[index];
    }

    public int getNumberOfComponents() {
        return this.components.length;
    }
}
