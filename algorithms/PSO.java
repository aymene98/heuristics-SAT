package algorithms;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

import model.*;

public class PSO {
	private ArrayList<Particule> particules = new ArrayList<Particule>();
	private Particule Gbest;
	private double c1,c2;
	private int populationSize;
	private int maxIterations = 0;
	private double w;
	private int numberOfVars = 0;
	private ArrayList<Clause> clauses = new ArrayList<Clause>();
	
	public PSO(int populationSize, int maxIterations, double c1, double c2, double w, String filePath){
		this.populationSize = populationSize;
		this.maxIterations = maxIterations;
		this.c1 = c1;
		this.c2 = c2;
		this.w = w;
		this.getClausesFromFile(filePath);
	}
	
	public Resultat solve(){
		double r1, r2;
		int iteration = 0,i;
		int max = 100, min = 0;
		int v,choice,numberOfchanges,index;
		double taux = (this.w - 0.4)/this.maxIterations;
		
		long startTime = System.currentTimeMillis();
		float elapsedTime;
		
		this.createRandomParticules();
		for (Particule p : this.particules) {
			p.setFitnessCurrent(eveluateFitness(p.getCurrent()));
			p.setFitnessBest(eveluateFitness(p.getBest()));
		}
		
		Collections.sort(this.particules);
		
		Gbest = new Particule(this.particules.get(0));
		Gbest.setFitnessCurrent(eveluateFitness(Gbest.getCurrent()));
		Gbest.setFitnessBest(eveluateFitness(Gbest.getBest()));
		
		System.out.println("Init best fitness : " + this.Gbest.getBest().getFitness());
		
		while (iteration <= this.maxIterations && Gbest.getBest().getFitness() != 0) {
			for (Particule p : this.particules) {
				
				/* calculating the velocity */
				r1 = Math.random() * (max - min + 1) + min;
				r2 = Math.random() * (max - min + 1) + min;
				v = (int) (w*p.getVelocity() + c1*r1*distance(p.getBest(),p.getCurrent()) + c2*r2*distance(Gbest.getCurrent(),p.getCurrent()));
				p.setVelocity(v);

				if(v >= ( (int) this.numberOfVars / 2) ){
					v = (int) this.numberOfVars / 2;
				}
				
				/****************** moving the particule at random or following the Gbest *******************/
				choice = (int) (Math.random() * (max - min + 1) + min);
				choice = choice % 2;

				if (choice == 0 && p.compareTo(Gbest) != 0) {
					// move according to Gbest
					numberOfchanges = 0;
					i = 0;
					while (numberOfchanges < v && i < this.numberOfVars) {
						i++;
						index = (int) (Math.random() * (this.numberOfVars - min ) + min);
						if (p.getCurrent().getSolution()[index] != Gbest.getBest().getSolution()[index]) {
							p.getCurrent().setSolutionIndex(index, Gbest.getBest().getSolution()[index]);
							numberOfchanges++;
						}
					}
				}else{
					// move at random
					numberOfchanges = 0;
					while (numberOfchanges < v) {
						index = (int) (Math.random() * (this.numberOfVars - min ) + min);
						if (p.getCurrent().getSolution()[index] == 0) {
							p.getCurrent().setSolutionIndex(index, 1);
						}else{
							p.getCurrent().setSolutionIndex(index, 0);
						}
						numberOfchanges++;
					}
				}
				/****************** evaluate fitness *******************/
				
				p.getCurrent().setFitness(eveluateFitness(p.getCurrent()));
				/****************** if greater then the Pbest update the Pbest. *******************/
				if (p.getCurrent().getFitness() < p.getBest().getFitness()) {
					Solution s = new Solution(this.numberOfVars);
					s.setSolution(p.getCurrent().getSolution());
					p.setBest(s);
				}
				
			}
			/* updating GBest */
			Collections.sort(this.particules);
			if (this.particules.get(0).getBest().getFitness() < Gbest.getBest().getFitness()) {
				Gbest = new Particule(this.particules.get(0));
				Gbest.setFitnessCurrent(eveluateFitness(Gbest.getCurrent()));
				Gbest.setFitnessBest(eveluateFitness(Gbest.getBest()));
			}
			
			this.w = this.w - taux;
			
			iteration++;

		}
		elapsedTime = System.currentTimeMillis() - startTime;
		elapsedTime = elapsedTime / 1000F;


		System.out.println("best fitness : " + this.Gbest.getBest().getFitness());
		return new ResultatPSO(this.Gbest.getBest().getFitness(), elapsedTime, this.maxIterations,this.c1,this.c2,this.w);
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
	
	
	private int eveluateFitness(Solution solution) {
		int numberOfFalseClauses = 0;
		int[] s = solution.getSolution();
		int j = 0;
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
	

	public void createRandomParticules(){
		for (int i = 0; i < this.populationSize; i++) {
			this.particules.add(new Particule(new Solution(numberOfVars)));
		}
	}

	
}
