package hr.fer.nenr.lab.hw3;

import java.io.*;
import java.util.Scanner;

public class FuzzyShipController {
    public static void main(String[] args) throws IOException {
        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));

        int L = 0, D = 0, LK = 0, DK = 0, V = 0, S = 0, acceleration, wheel;

        // STRUCTURES
        Defuzzifier def = new COADefuzzifier();

        // Creating two systems:
        // Two bases of rules are built and all is initialized
        FuzzySystem accFS = new AccFuzzySystemMin(def);
        FuzzySystem whFS = new WhFuzzySystemMin(def);

        // MAIN LOOP
        String line;
        while (true) {
            if ((line = input.readLine()) != null) {
                if (line.charAt(0) == 'K') break;

                Scanner s = new Scanner(line);
                L = s.nextInt();
                D = s.nextInt();
                LK = s.nextInt();
                DK = s.nextInt();
                V = s.nextInt();
                S = s.nextInt();
            }

            // fuzzy magic ...
            acceleration = accFS.infer(L, D, LK, DK, V, S);
            wheel = whFS.infer(L, D, LK, DK, V, S);

            //acceleration = 10;
            //wheel = 5;
            System.out.println(acceleration + " " + wheel);
            System.out.flush();
        }
    }
}
