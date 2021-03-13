package hr.fer.nenr.lab.hw1;

public class Domains {
    public static void main(String[] args) {
        IDomain d1 = Domain.intRange(0, 5); // {0, 1, 2, 3, 4}
        Debug.print(d1, "Domain d1:");

        IDomain d2 = Domain.intRange(0, 3); // {0, 1, 2}
        Debug.print(d2, "Domain d2:");

        IDomain d3 = Domain.combine(d1, d2);
        Debug.print(d3, "Domain d3:");

        System.out.println(d3.elementForIndex(0));
        System.out.println(d3.elementForIndex(5));
        System.out.println(d3.elementForIndex(14));
        System.out.println(d3.indexOfElement(DomainElement.of(4, 1)));
    }
}
