/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package primjer;
import java.io.*;
import java.util.Scanner;

public class Main {

    
    public static void main(String[] args) throws IOException {
           BufferedReader input = new BufferedReader(new InputStreamReader(System.in));

	    int L=0,D=0,LK=0,DK=0,V=0,S=0,akcel,kormilo;
	    String line = null;
		while(true){
			if((line = input.readLine())!=null){
				if(line.charAt(0)=='K') break;
				Scanner s = new Scanner(line);
				L = s.nextInt();
				D = s.nextInt();
				LK = s.nextInt();
				DK = s.nextInt();
				V = s.nextInt();
				S = s.nextInt();
	        }

	        // fuzzy magic ...

	        akcel = 10; kormilo = 5;
	        System.out.println(akcel + " " + kormilo);
	        System.out.flush();
	   }
    }

}

