package main;

import algorithms.BSO;
import algorithms.Genetic;
import algorithms.PSO;

public class Main {

	public static void main(String[] args) {
		System.out.println("Satisfiable file -- Genetic algorithm --");
		for (int i = 1; i < 10; i++) {
			String file = "sat/uf75-0"+i+".cnf";
			
			Genetic g = new Genetic(100, 7000, 0.01, 0.1, 1000, file);
			System.out.println(g.solve());
			
		}
		System.out.println("\nUnsatisfiable file -- Genetic algorithm --");
		for (int i = 1; i < 10; i++) {
			String file = "unsat/uuf75-0"+i+".cnf";

			Genetic g = new Genetic(100, 7000, 0.01, 0.1, 1000, file);
			System.out.println(g.solve());

		}

		System.out.println("\nSatisfiable file -- PSO algorithm --");
		for (int i = 1; i < 10; i++) {
			String file = "sat/uf75-0"+i+".cnf";
			
			PSO p = new PSO(100, 10000, 1.5 , 2, 0.9, file); // 100 particules 9000 iteration all sat.
			System.out.println(p.solve());
			
		}
		System.out.println("\nUnsatisfiable file -- PSO algorithm --");
		for (int i = 1; i < 10; i++) {
			String file = "unsat/uuf75-0"+i+".cnf";

			PSO p = new PSO(100, 10000, 1.5 , 2, 0.9, file); // 100 particules 9000 iteration all sat.
			System.out.println(p.solve());

		}


		System.out.println("\nSatisfiable file -- BSO algorithm --");
		for (int i = 1; i < 10; i++) {
			String file = "sat/uf75-0"+i+".cnf";
			
			BSO b = new BSO(10, 30, 5000, 25, 5, file);
			System.out.println(b.solve());
			
		}

		System.out.println("\nUnsatisfiable file -- BSO algorithm --");
		for (int i = 1; i < 10; i++) {
			String file = "unsat/uuf75-0"+i+".cnf";

			BSO b = new BSO(10, 30, 5000, 25, 5, file);
			System.out.println(b.solve());
		}

	}

}
