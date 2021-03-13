package hr.fer.nenr.lab.hw3;

import hr.fer.nenr.lab.hw1.*;

public class WhFuzzySystemMin implements FuzzySystem {
    private Defuzzifier def;
    private Rule[] rules;

    public WhFuzzySystemMin(Defuzzifier def) {
        this.def = def;

        // INITIALIZE RULES & BASE

        // constraints & domains:
        int max_distance = 2000, max_a = 35, ship_r = 10;
        IDomain d_distance = Domain.intRange(0, max_distance + 1);
        IDomain d_orientation = Domain.intRange(0, 2);
        IDomain d_angle = Domain.intRange(-90, 91);

        // sets:
        IFuzzySet diagonally_close = new CalculatedFuzzySet(d_distance,
                StandardFuzzySets.lFunction(
                        d_distance.indexOfElement(DomainElement.of(max_a + 2 * ship_r)),
                        d_distance.indexOfElement(DomainElement.of(max_a + 4 * ship_r))));
        IFuzzySet side_close = new CalculatedFuzzySet(d_distance,
                StandardFuzzySets.lFunction(
                        d_distance.indexOfElement(DomainElement.of(15)),
                        d_distance.indexOfElement(DomainElement.of(20))
                ));
        IFuzzySet wrong_way = new CalculatedFuzzySet(d_orientation,
                StandardFuzzySets.lFunction(
                        d_orientation.indexOfElement(DomainElement.of(0)),
                        d_orientation.indexOfElement(DomainElement.of(1))));
        IFuzzySet turn_right = new CalculatedFuzzySet(d_angle,
                StandardFuzzySets.lFunction(
                        d_angle.indexOfElement(DomainElement.of(-70)),
                        d_angle.indexOfElement(DomainElement.of(-60))));
        IFuzzySet turn_left = new CalculatedFuzzySet(d_angle,
                StandardFuzzySets.gammaFunction(
                        d_angle.indexOfElement(DomainElement.of(60)),
                        d_angle.indexOfElement(DomainElement.of(70))));
        IFuzzySet turn_around = new CalculatedFuzzySet(d_angle,
                StandardFuzzySets.gammaFunction(
                        d_angle.indexOfElement(DomainElement.of(89)),
                        d_angle.indexOfElement(DomainElement.of(90))));

        // rules:
        // R1: If LK is "diagonally_close" THEN K is "turn_right".
        IFuzzySet[] premises1 = {null, null, diagonally_close, null, null, null};
        Rule r1 = new Rule(premises1, turn_right);
        // R2: If DK is "diagonally_close" THEN K is "turn_left".
        IFuzzySet[] premises2 = {null, null, null, diagonally_close, null, null};
        Rule r2 = new Rule(premises2, turn_left);
        // R3: If S is "wrong_way" THEN K is "turn_around".
        IFuzzySet[] premises3 = {null, null, null, null, null, wrong_way};
        Rule r3 = new Rule(premises3, turn_around);
        // R4: If L is "side_close" THEN K is "turn_right"
        IFuzzySet[] premises4 = {side_close, null, null, null, null, turn_right};
        Rule r4 = new Rule(premises4, turn_right);
        // R5: If R is "side_close" THEN K is "turn_left"
        IFuzzySet[] premises5 = {null, side_close, null, null, null, turn_left};
        Rule r5 = new Rule(premises5, turn_left);
        this.rules = new Rule[5];
        this.rules[0] = r1;
        this.rules[1] = r2;
        this.rules[2] = r3;
        this.rules[3] = r4;
        this.rules[4] = r5;
    }

    public int infer(int... inputs) {
        if (this.rules.length == 0) return 0;

        IFuzzySet y, A = this.rules[0].apply(inputs);

        for (int i = 1; i < this.rules.length; i++) {
            y = this.rules[i].apply(inputs);
            A = Operations.binaryOperation(A, y, Operations.zadehOr());
        }
        return def.defuzzify(A).getComponentValue(0);
    }
}
