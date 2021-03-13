package hr.fer.nenr.lab.hw2;

import hr.fer.nenr.lab.hw1.*;

public class Relations {
    public static boolean isUtimesURelation(IFuzzySet relation) {
        IDomain u1 = relation.getDomain().getComponent(0);
        IDomain u2 = relation.getDomain().getComponent(1);
        if (u1.getCardinality() != u2.getCardinality()) return false;

        for (DomainElement e : u1) {
            if (!e.equals(u2.elementForIndex(u1.indexOfElement(e)))) return false;
        }
        return true;
    }

    public static boolean isSymmetric(IFuzzySet relation) {
        IDomain domain = relation.getDomain();
        for (DomainElement e : domain) {
            // test (2, 1) != (1, 2)? -> return false
            if (relation.getValueAt(e) != relation.getValueAt(DomainElement.of(e.getComponentValue(1), e.getComponentValue(0))))
                return false;
        }
        return true;
    }

    public static boolean isReflexive(IFuzzySet relation) {
        IDomain domain = relation.getDomain();
        for (DomainElement e : domain) {
            // test (x, x) != 1? -> return false
            if (e.getComponentValue(0) == e.getComponentValue(1) && relation.getValueAt(e) != 1.0)
                return false;
        }
        return true;
    }

    public static boolean isMaxMinTransitive(IFuzzySet relation) {
        IFuzzySet composition = Relations.compositionOfBinaryRelations(relation, relation);
        for (DomainElement e : composition.getDomain()) {
            if (relation.getValueAt(e) < composition.getValueAt(e)) return false;
        }
        return true;
    }

    public static IFuzzySet compositionOfBinaryRelations(IFuzzySet r1, IFuzzySet r2) {
        int uLength = r1.getDomain().getComponent(0).getCardinality();
        int vLength = r1.getDomain().getComponent(1).getCardinality();
        int wLength = r2.getDomain().getComponent(1).getCardinality();

        // creating corresponding matrices for easier calculation of sup-min composition
        double[][] r1matrix = new double[uLength][vLength];
        double[][] r2matrix = new double[vLength][wLength];

        int i, j;
        for (DomainElement e : r1.getDomain()) {
            i = r1.getDomain().getComponent(0).indexOfElement(DomainElement.of(e.getComponentValue(0)));
            j = r1.getDomain().getComponent(1).indexOfElement(DomainElement.of(e.getComponentValue(1)));
            r1matrix[i][j] = r1.getValueAt(e);
        }
        for (DomainElement e : r2.getDomain()) {
            i = r2.getDomain().getComponent(0).indexOfElement(DomainElement.of(e.getComponentValue(0)));
            j = r2.getDomain().getComponent(1).indexOfElement(DomainElement.of(e.getComponentValue(1)));
            r2matrix[i][j] = r2.getValueAt(e);
        }

        double[][] compositionValues = new double[uLength][wLength];
        double[] minValues = new double[vLength];
        for (i = 0; i < uLength; i++) {
            for (j = 0; j < wLength; j++) {
                for (int k = 0; k < vLength; k++) {
                    minValues[k] = Math.min(r1matrix[i][k], r2matrix[k][j]);
                }
                compositionValues[i][j] = maxOfArray(minValues);
            }
        }

        // assigning  values from matrix to the result relation
        MutableFuzzySet compositionRelation = new MutableFuzzySet(Domain.combine(r1.getDomain().getComponent(0), r2.getDomain().getComponent(1)));
        for (DomainElement e : compositionRelation.getDomain()) {
            compositionRelation = compositionRelation.set(e, compositionValues
                    [compositionRelation.getDomain().getComponent(0).indexOfElement(DomainElement.of(e.getComponentValue(0)))]
                    [compositionRelation.getDomain().getComponent(1).indexOfElement(DomainElement.of(e.getComponentValue(1)))]);
        }
        return compositionRelation;
    }

    public static boolean isFuzzyEquivalence(IFuzzySet r) {
        return Relations.isSymmetric(r) && Relations.isReflexive(r) && Relations.isMaxMinTransitive(r);
    }

    public static double maxOfArray(double[] array) {
        double max = 0;
        for (double value : array) {
            if (value > max) max = value;
        }
        return max;
    }
}
