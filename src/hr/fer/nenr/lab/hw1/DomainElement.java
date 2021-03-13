package hr.fer.nenr.lab.hw1;

import java.util.Arrays;

public class DomainElement {
    private int[] values;

    public DomainElement(int[] values) {
        this.values = values;
    }

    public int getNumberOfComponents() {
        return this.values.length;
    }

    public int getComponentValue(int index) {
        return this.values[index];
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DomainElement that = (DomainElement) o;
        return Arrays.equals(values, that.values);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(values);
    }

    @Override
    public String toString() {
        if (this.values.length == 1) return Integer.toString(this.values[0]);

        StringBuilder sb = new StringBuilder();
        sb.append("(");
        for (int i = 0; i < this.values.length; i++) {
            sb.append(Integer.toString(this.values[i]));
            if (i != this.values.length - 1) sb.append(", ");
        }
        sb.append(")");
        return sb.toString();
    }

    public static DomainElement of(int... values) {
        return new DomainElement(values);
    }
}
