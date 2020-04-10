package model;

import java.util.Arrays;

public class Solution implements Comparable<Solution>{
	private int solution[];
	private int fitness,numberOfVars;
	
	public Solution(int numberOfVars){
		this.solution = new int[numberOfVars];
		this.numberOfVars = numberOfVars;
		this.fitness = numberOfVars;
		this.randomSolution();
	}
	
	
	public void randomSolution() {
	    int max = 100, min = -100;
	    int x;
	    for (int i = 0; i < numberOfVars; i++) {

	    	x = (int) (Math.random() * (max - min + 1) + min);
	    	if (x > 0) {
				x = 1; // existe sous la forme positive.
			} else {
				x = 0; // existe sous la forme négative
			} // -1 n'existe pas.
	    	solution[i] = x;
	    	this.fitness = (int) (Math.random() * (max - min + 1) + min);
		}
	}

	public int[] getSolution() {
		return solution;
	}
	

	public void setSolution(int[] solution) {
		this.solution = new int[solution.length];
		for (int i = 0; i < solution.length; i++) {
			this.solution[i] = solution[i];
		}
		
	}

	public int getFitness() {
		return fitness;
	}

	public void setFitness(int fitness) {
		this.fitness = fitness;
	}


	@Override
	public String toString() {
		return "Solution [solution=" + Arrays.toString(solution) + ", fitness=" + fitness + "]";
	}


	@Override
	public int compareTo(Solution o) {
		return (this.fitness - o.getFitness());
	}


	public int getNumberOfVars() {
		return numberOfVars;
	}


	public void setNumberOfVars(int numberOfVars) {
		this.numberOfVars = numberOfVars;
	}
	
	public void setSolutionIndex(int index, int value){
		this.solution[index] = value;
	}
	
	
}
