package hr.fer.nenr.lab.hw1;

public class Operations {
    public static IFuzzySet unaryOperation(IFuzzySet set, IUnaryFunction uFunc) {
        IDomain domain = set.getDomain();
        MutableFuzzySet resultSet = new MutableFuzzySet(set.getDomain());

        for (DomainElement e : domain) {
            resultSet = resultSet.set(e, uFunc.valueAt(set.getValueAt(e)));
        }

        return resultSet;
    }

    public static IFuzzySet binaryOperation(IFuzzySet set1, IFuzzySet set2, IBinaryFunction biFunc) {
        IDomain domain = set1.getDomain();
        MutableFuzzySet resultSet = new MutableFuzzySet(domain);

        for (DomainElement e : domain) {
            resultSet = resultSet.set(e, biFunc.valueAt(set1.getValueAt(e), set2.getValueAt(e)));
        }

        return resultSet;
    }

    public static IUnaryFunction zadehNot() {
        return new ZadehNot();
    }

    public static IBinaryFunction zadehOr() {
        return new ZadehOr();
    }

    public static IBinaryFunction zadehAnd() {
        return new ZadehAnd();
    }

    public static IBinaryFunction hamacherTNorm(double value) {
        return new HamacherTNorm(value);
    }

    public static IBinaryFunction hamacherSNorm(double value) {
        return new HamacherSNorm(value);
    }
}
