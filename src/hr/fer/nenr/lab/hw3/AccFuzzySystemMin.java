package hr.fer.nenr.lab.hw3;

import hr.fer.nenr.lab.hw1.*;

public class AccFuzzySystemMin implements FuzzySystem {
    private Defuzzifier def;
    private Rule[] rules;

    public AccFuzzySystemMin(Defuzzifier def) {
        this.def = def;

        // INITIALIZE RULES & BASE

        // constraints & domains:
        int max_vel = 1000, max_a = 35;
        IDomain d_velocity = Domain.intRange(0, max_vel + 1);
        IDomain d_acc = Domain.intRange(-max_a, max_a + 1);

        // sets:
        IFuzzySet too_slow = new CalculatedFuzzySet(d_velocity, StandardFuzzySets.lFunction(
                d_velocity.indexOfElement(DomainElement.of(40)),
                d_velocity.indexOfElement(DomainElement.of(70))));
        IFuzzySet too_fast = new CalculatedFuzzySet(d_velocity, StandardFuzzySets.gammaFunction(
                d_velocity.indexOfElement(DomainElement.of(70)),
                d_velocity.indexOfElement(DomainElement.of(90))));
        IFuzzySet accelerate = new CalculatedFuzzySet(d_acc, StandardFuzzySets.gammaFunction(
                d_acc.indexOfElement(DomainElement.of((int) Math.round(0.7 * max_a))),
                d_acc.indexOfElement(DomainElement.of(max_a))));
        IFuzzySet decelerate = new CalculatedFuzzySet(d_acc, StandardFuzzySets.lFunction(
                d_acc.indexOfElement(DomainElement.of(-max_a)),
                d_acc.indexOfElement(DomainElement.of((int) Math.round(-0.7 * max_a)))));

        // rules:
        // R1: If V is "too_slow" THEN A is "accelerate".
        IFuzzySet[] premises1 = {null, null, null, null, too_slow, null};
        Rule r1 = new Rule(premises1, accelerate);
        // R2: If V is "too_fast" THEN A is "decelerate".
        IFuzzySet[] premises2 = {null, null, null, null, too_fast, null};
        Rule r2 = new Rule(premises2, decelerate);
        this.rules = new Rule[2];
        rules[0] = r1;
        rules[1] = r2;
    }

    public int infer(int... inputs) {
        if (this.rules.length == 0) return 0;

        IFuzzySet y, A = this.rules[0].apply(inputs);

        if (this.rules.length == 1) def.defuzzify(A).getComponentValue(0);

        for (int i = 1; i < this.rules.length; i++) {
            y = this.rules[i].apply(inputs);
            A = Operations.binaryOperation(A, y, Operations.zadehOr());
        }
        return def.defuzzify(A).getComponentValue(0);
    }
}
