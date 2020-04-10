package algorithms;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.Scanner;

import model.Clause;
import model.Resultat;
import model.ResultatGenetic;
import model.Solution;

public class Genetic {
	private ArrayList<Solution> population = new ArrayList<Solution>();
	private int populationSize;
	private int maxIterations = 0;
	private double mutationRate = 0;
	private double crossRate = 0;
	private int numberOfVars = 0;
	private int maxPopulationAllowed;
	private ArrayList<Clause> clauses = new ArrayList<Clause>();
	private Random rand = new Random();
	
	public Genetic(int populationSize, int maxIterations, double mutationRate, double crossrate, int maxPopulationAllowed, String filePath){
		this.populationSize = populationSize;
		this.maxIterations = maxIterations;
		this.mutationRate = mutationRate;
		this.crossRate = crossrate;
		this.maxPopulationAllowed = maxPopulationAllowed;
		rand.setSeed(234);
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
		    	/*chaque ligne est une clause */
		    	data = myReader.nextLine();
		    	String literals[] = data.split(" ");
		    	if (i==0) {
					for (int j = 0; j < literals.length - 1; j++) {
						literals[j] = literals[j+1];
					}
				}
		    	Clause c = new Clause(numberOfVarsInFile);
		    	/*negative => 0; positive => 1*/
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
		int initFitness = 75;
		double mutation, cross;
		Solution parent1,parent2,bestchild,parent3,parent4;
		ArrayList<Solution> children = new ArrayList<Solution>();
		ArrayList<Solution> tempS = new ArrayList<Solution>();
		long startTime = System.currentTimeMillis();
		float elapsedTime;
		
		this.createRandomPopulation(); 
		for (Solution solution : this.population) {
			solution.setFitness(eveluateFitness(solution));
		}
		Collections.sort(this.population);

		int indexMinSolution = 0;
		initFitness = this.population.get(0).getFitness();
		
		while (iteration <= this.maxIterations && this.population.get(0).getFitness() != 0) {
			mutation = Math.random();
			cross = Math.random();
			
			Collections.sort(this.population);

			parent1 = this.population.get(0);
			parent2 = this.population.get(1);
			parent3 = this.population.get(2);
			parent4 = this.population.get(3);

			/* mutate and cross */
			if(cross >= this.crossRate){
				children = cross(parent1,parent2);
				children.addAll(cross(parent2,parent3));
				children.addAll(cross(parent3,parent4));
			}
			else{
				children = new ArrayList<Solution>();
			}

			children.add(parent2);
			children.add(parent1);

			if(mutation >= this.mutationRate){
				children.addAll(mutate(children));
			}
			
			for (Solution solution : children) {
				solution.setFitness(eveluateFitness(solution));
			}

			this.population.addAll(children);
			
			Collections.sort(this.population);
			//this.population.remove(this.populationSize);

			if (this.population.size() > this.maxPopulationAllowed) {
				this.population = new ArrayList<Solution>(this.population.subList(0, this.maxPopulationAllowed)); 
			}
			
			iteration++;
			
		}
		elapsedTime = System.currentTimeMillis() - startTime;
		elapsedTime = elapsedTime / 1000F;
		Collections.sort(this.population);
		Solution best = this.population.get(0);

		System.out.println("Init fitness : " + initFitness);
		System.out.println("Best fitness obtained : " + best.getFitness());
		System.out.println("Final population size : " + this.population.size());
		return new ResultatGenetic(best.getFitness(), elapsedTime, this.maxIterations, this.crossRate, this.mutationRate, this.population.size());
	}
	
	private ArrayList<Solution> mutate(ArrayList<Solution> children) {
		int index;
		int max = this.numberOfVars - 1, min = 0;
		int temp[] = new int[this.numberOfVars];
		ArrayList<Solution> tempA = new ArrayList<Solution>(children); 
		
		for (Solution solution : tempA) {
			index = (int) (Math.random() * (max - min + 1) + min);;

			for (int i = 0; i < this.numberOfVars; i++) {
				temp[i] = solution.getSolution()[i];
			}
			if (temp[index] == 0) {
				temp[index] = 1;
			} else {
				temp[index] = 0;
			}
			solution.setSolution(temp);;
		}
		return tempA;
	}

	private ArrayList<Solution> cross(Solution parent1, Solution parent2) {
		ArrayList<Solution> children = new ArrayList<Solution>();
		Solution s1 = new Solution(this.numberOfVars),s2 = new Solution(this.numberOfVars),s3 = new Solution(this.numberOfVars);
		Solution s4 = new Solution(this.numberOfVars),s5 = new Solution(this.numberOfVars),s6 = new Solution(this.numberOfVars);
		int child[] = new int[this.numberOfVars];
		int p1[] = parent1.getSolution();
		int p2[] = parent2.getSolution();
		
		int max1 = this.numberOfVars, min1 = 0;
		int index1 = (int) (Math.random() * (max1 - min1 + 1) + min1);
		
		int max2 = this.numberOfVars, min2 = index1;
		int index2 = (int) (Math.random() * (max2 - min2 + 1) + min2);
		/************************************************************************/
		
		for (int i = 0; i < index1; i++) {
			child[i] = p1[i];
		}
		
		for (int i = index1; i < index2; i++) {
			child[i] = p1[i];
		}
		
		for (int i = index2; i < child.length; i++) {
			child[i] = p2[i];
		}
		
		s1.setSolution(child);
		children.add(s1);
		/************************************************************************/
		for (int i = 0; i < index1; i++) {
			child[i] = p2[i];
		}
		
		for (int i = index1; i < index2; i++) {
			child[i] = p2[i];
		}
		
		for (int i = index2; i < child.length; i++) {
			child[i] = p1[i];
		}
		
		s2.setSolution(child);
		children.add(s2);
		/************************************************************************/
		for (int i = 0; i < index1; i++) {
			child[i] = p2[i];
		}
		
		for (int i = index1; i < index2; i++) {
			child[i] = p1[i];
		}
		
		for (int i = index2; i < child.length; i++) {
			child[i] = p1[i];
		}
		
		s3.setSolution(child);
		children.add(s3);
		/************************************************************************/
		for (int i = 0; i < index1; i++) {
			child[i] = p1[i];
		}
		
		for (int i = index1; i < index2; i++) {
			child[i] = p2[i];
		}
		
		for (int i = index2; i < child.length; i++) {
			child[i] = p2[i];
		}
		
		s4.setSolution(child);
		children.add(s4);
		/************************************************************************/
		for (int i = 0; i < index1; i++) {
			child[i] = p1[i];
		}
		
		for (int i = index1; i < index2; i++) {
			child[i] = p2[i];
		}
		
		for (int i = index2; i < child.length; i++) {
			child[i] = p1[i];
		}
		
		s5.setSolution(child);
		children.add(s5);
		/************************************************************************/
		for (int i = 0; i < index1; i++) {
			child[i] = p2[i];
		}
		
		for (int i = index1; i < index2; i++) {
			child[i] = p1[i];
		}
		
		for (int i = index2; i < child.length; i++) {
			child[i] = p2[i];
		}
		
		s6.setSolution(child);
		children.add(s6);
		/************************************************************************/
		
		return children;
	}

	private int eveluateFitness(Solution solution) {
		int numberOfFalseClauses = 0;
		int[] s = solution.getSolution();
		int j;
		int[] clauseArray;
		boolean satClause;
		for (Clause c : this.clauses) {
			clauseArray = c.getClause();
			j = 0;
			satClause = false;
			while(!satClause && j<numberOfVars){
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
	

	public void createRandomPopulation(){
		for (int i = 0; i < this.populationSize; i++) {
			this.population.add(new Solution(numberOfVars));
		}
	}

	public int getPopulationSize() {
		return populationSize;
	}

	public ArrayList<Solution> getPopulation() {
		return population;
	}

	public int getMaxIterations() {
		return maxIterations;
	}

	public void setMaxIterations(int maxIterations) {
		this.maxIterations = maxIterations;
	}


	public double getMutationRate() {
		return mutationRate;
	}


	public void setMutationRate(int mutationRate) {
		this.mutationRate = mutationRate;
	}


	public double getCrossRate() {
		return crossRate;
	}


	public void setCrossRate(int crossRate) {
		this.crossRate = crossRate;
	}


	public void setPopulation(ArrayList<Solution> population) {
		this.population = population;
	}


	public void setPopulationSize(int populationSize) {
		this.populationSize = populationSize;
	}


	public ArrayList<Clause> getClauses() {
		return clauses;
	}


	public void setClauses(ArrayList<Clause> clauses) {
		this.clauses = clauses;
	}
	

}
