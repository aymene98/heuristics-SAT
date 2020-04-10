package algorithms;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Scanner;

import model.Clause;
import model.Resultat;
import model.ResultatBSO;
import model.Solution;

public class BSO {
	private int flips;
	private int maxIterations = 0;
	private int numberOfVars = 0;
	private int numberOfLocalSearchIterations;
	private int nbChances = 0;
	private int MaxChances = 0;
	private int beeNumber = 0;
	private ArrayList<Clause> clauses = new ArrayList<Clause>();
	
	public BSO(int bees, int flips, int maxIterations, int numberOfLocalSearchIterations, int nbChances ,String filePath){
		this.maxIterations = maxIterations;
		this.beeNumber = bees;
		this.flips = flips;
		if (this.flips > 75) {
			this.flips = 75;
		}
		this.numberOfLocalSearchIterations = numberOfLocalSearchIterations;
		this.nbChances = nbChances;
		this.MaxChances = nbChances;
		if (numberOfLocalSearchIterations > 75) {
			this.numberOfLocalSearchIterations = 75;
		}
		this.getClausesFromFile(filePath);
	}
	
	private void getClausesFromFile(String filePath){
		int numberOfVarsInFile = 75;
		try {
			String path = "./src/" + filePath;
		    File myObj = new File(path);
		    Scanner myReader = new Scanner(myObj);
		    String data;
		    for (int i = 0; i < 8; i++) {
		    	  data = myReader.nextLine();
			}
		    data = "";
		    /* Header removed */
		    
		    int i = 0;
		    while (myReader.hasNextLine() && i < 325) {
		    	/* chaque ligne est une clause */
		    	data = myReader.nextLine();
		    	String literals[] = data.split(" ");
		    	if (i==0) {
					for (int j = 0; j < literals.length - 1; j++) {
						literals[j] = literals[j+1];
					}
				}
		    	Clause c = new Clause(numberOfVarsInFile);
		    	/* negative => 0; positive => 1 */
		    	int index = 0;
		    	for (int j = 0; j < 3; j++) {
		    		index = Integer.parseInt(literals[j]);
		    		if(index<0){
		    			index = (-1) * index - 1;
		    			c.setValue(index, 0);
		    		}
		    		else{
		    			index = index - 1;
		    			c.setValue(index, 1);
		    		}
				}
		    	clauses.add(c);
		    	i++;
		    }
		    myReader.close();
		} catch (FileNotFoundException e) {
			System.out.println("An error occurred.");
		    e.printStackTrace();
		}
		this.numberOfVars = numberOfVarsInFile;
		
	}
	
	
	public Resultat solve(){
		int iteration = 0;
		ArrayList<Solution> taboo = new ArrayList<Solution>();
		ArrayList<Solution> searchSpace = new ArrayList<Solution>();
		ArrayList<Solution> dance;
		Solution Sref = new Solution(this.numberOfVars), best = new Solution(this.numberOfVars);
		Sref.setFitness(eveluateFitness(Sref));
		System.out.println("Init fitness : " + Sref.getFitness());
		
		float elapsedTime;
		long startTime = System.currentTimeMillis();
		int c = 0;
		while(iteration < this.maxIterations && Sref.getFitness() != 0){
			
			dance = new ArrayList<Solution>();
			/*insert Sref into taboo */
			taboo.add(Sref);

			/* determine search points from Sref */
			/* number of flips is the number of bees */ 
			searchSpace = flip(Sref,this.flips);
			
			for (Solution solution : searchSpace) {
				/* explore the neighborhood with a bee */
				dance.addAll(explore(solution));
			}
			dance.addAll(searchSpace);
			
			Collections.sort(dance);
			
			best = new Solution(dance.get(0).getSolution().length);
			best.setSolution(dance.get(0).getSolution());
			best.setFitness(eveluateFitness(best));

			Sref = chooseSref(Sref,best,taboo,dance);
			iteration++;
		}
		elapsedTime = System.currentTimeMillis() - startTime;
		elapsedTime = elapsedTime / 1000F;
		
		System.out.println("Best fitness : " + Sref.getFitness());

		return new ResultatBSO(Sref.getFitness(),elapsedTime,this.maxIterations,this.beeNumber,this.flips,this.numberOfLocalSearchIterations,this.nbChances);
	}
	
	

	public Solution chooseSref(Solution sref, Solution best, ArrayList<Solution> taboo, ArrayList<Solution> dance) {
		if(best.getFitness() <= sref.getFitness()){
			this.nbChances = this.MaxChances;
			// return best fitness in dance and not in taboo
			boolean isInTaboo = false;
			for (Solution solution : dance) {
				for (Solution sol : taboo) {
					if(Arrays.equals(solution.getSolution(), sol.getSolution())){
						isInTaboo = true;
					}
				}
				if(!isInTaboo){
					Solution s = new Solution(75);
					s.setSolution(solution.getSolution());
					s.setFitness(eveluateFitness(s));
					return s;
				}
			}
		}
		else{
			this.nbChances--;
			if(this.nbChances <= 0){
				// return best fitness in dance and not in taboo 
				boolean isInTaboo = false;
				for (Solution solution : dance) {
					for (Solution sol : taboo) {
						if(Arrays.equals(solution.getSolution(), sol.getSolution())){
							isInTaboo = true;
						}
					}
					if(!isInTaboo){
						Solution s = new Solution(75);
						s.setSolution(solution.getSolution());
						s.setFitness(eveluateFitness(s));
						return s;
					}
				}
			}else{
				// return solution with max distance in dance with elements in dance
				int indexMax = 0;
				ArrayList<Integer> mins = new ArrayList<Integer>();
				int min = 0,temp = 0;

				// looking for the diversity of each solution (diversity is the min of distances)
				for (int i = 0; i < dance.size()-2; i++) {
					min = distance(dance.get(i),dance.get(i+1));
					for (int j = i+2; j < dance.size(); j++) {
						temp = distance(dance.get(i),dance.get(j));
						if(temp < min){
							min = temp;
						}
					}
					mins.add(min);
				}

				// looking for the solution that has the max diversity this solution will be our new Sref.
				int max = mins.get(0); 
				int index = 0;
				for (int i = 0; i < mins.size(); i++) {
					if(min > max){
						max = min;
						index = i;
					}
				}
				Solution s = new Solution(75);
				s.setSolution(dance.get(index).getSolution());
				s.setFitness(eveluateFitness(s));
				return s;
			}
			
		}
		return best;
	}
	
	public int distance(Solution best, Solution current) {
		int nbr = 0;
		for (int i = 0; i < best.getSolution().length - 1; i++) {
			if (best.getSolution()[i] != current.getSolution()[i]) {
				nbr++;
			}
		}
		return nbr;
	}

	public ArrayList<Solution> explore(Solution sol) {
		int solution[] = new int[sol.getSolution().length];
		ArrayList<Solution> resFlips = new ArrayList<Solution>();
		Solution s;
		int index;
		int max = this.numberOfVars - 1, min = 0;
		
		for (int j = 0; j < this.numberOfLocalSearchIterations; j++) {
			
			for (int i = 0; i < solution.length; i++) {
				solution[i] = sol.getSolution()[i];
			}
			
			index = (int) (Math.random() * (max - min + 1) + min);
			
			if(solution[index]==1){
				solution[index] = 0;
			}
			else{
				solution[index] = 1;
			}
			
			s = new Solution(sol.getNumberOfVars());
			s.setSolution(solution);
			s.setFitness(eveluateFitness(s));
			resFlips.add(s);
		}

		return resFlips;
	}

	public ArrayList<Solution> flip(Solution sref, int flips) {
		int solution[] = new int[this.numberOfVars];
		ArrayList<Solution> resFlips = new ArrayList<Solution>();
		Solution s;

		
		for (int j = 0; j < flips; j++) {
			
			for (int i = 0; i < solution.length; i++) {
				solution[i] = sref.getSolution()[i];
			}
			
			int i = j;
			while(i < solution.length){
				if(solution[i]==1){
					solution[i] = 0;
				}
				else{
					solution[i] = 1;
				}
				i = i + flips;
			}
			
			s = new Solution(sref.getNumberOfVars());
			s.setSolution(solution);
			
			s.setFitness(eveluateFitness(s));
			resFlips.add(s);
		}
		
		Collections.sort(resFlips);
		return new ArrayList<Solution>(resFlips.subList(0, this.beeNumber));
	}

	private int eveluateFitness(Solution solution) {
		int numberOfFalseClauses = 0;
		int[] s = solution.getSolution();
		int j = 0;
		int[] clauseArray;
		boolean satClause;
		for (Clause c : this.clauses) {
			clauseArray = c.getClause();
			//System.out.println(c);
			j = 0;
			satClause = false;
			while(!satClause && j<this.numberOfVars){
				//System.out.println(j);
				if(clauseArray[j] == s[j]){
					satClause = true;
				}else{
					j++;
				}
			}
			if (!satClause) {
				numberOfFalseClauses++;
			}
		}
		return numberOfFalseClauses;
	}

	public int getMaxIterations() {
		return maxIterations;
	}

	public void setMaxIterations(int maxIterations) {
		this.maxIterations = maxIterations;
	}

	public ArrayList<Clause> getClauses() {
		return clauses;
	}


	public void setClauses(ArrayList<Clause> clauses) {
		this.clauses = clauses;
	}
	

}
