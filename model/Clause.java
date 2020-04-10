package model;

import java.util.Arrays;

public class Clause {
	private int clause[];
	private int numberOfVars;

	public Clause(int numberOfVars){
		this.numberOfVars = numberOfVars;
		this.clause = new int[numberOfVars];
		Arrays.fill(clause, -1);
	}

	public int[] getClause() {
		return clause;
	}

	public void setClause(int[] clause) {
		this.clause = clause;
	}
	
	public void setValue(int index, int value){
		this.clause[index] = value;
	}

	public int getNumberOfVars() {
		return numberOfVars;
	}

	public void setNumberOfVars(int numberOfVars) {
		this.numberOfVars = numberOfVars;
	}

	@Override
	public String toString() {
		return "Clause [clause=" + Arrays.toString(clause) + "]";
	}
	
	
	
	
}
