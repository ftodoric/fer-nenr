package hr.fer.nenr.lab.hw1;

public class Debug {
    public static void print(IDomain domain, String headingText) {
        if (headingText != null) {
            System.out.println(headingText);
        }
        for (DomainElement e : domain) {
            System.out.println("- " + e);
        }
        System.out.println("Cardinality: " + domain.getCardinality());
        System.out.println();
    }

    public static void print(IFuzzySet set, String headingText) {
        if (headingText != null) {
            System.out.println(headingText);
        }

        IDomain domain = set.getDomain();
        for (DomainElement e : domain) {
            System.out.print("d(" + e + ") = ");
            System.out.printf("%f%n", set.getValueAt(e));
        }
        System.out.println("Cardinality: " + domain.getCardinality());
        System.out.println();
    }
}
