package model;

import java.text.DecimalFormat;

public class Resultat {
	private String algo;
	private int fitness;
	private double tempsEcourle;
	private int nbIteration;
	private static DecimalFormat df2 = new DecimalFormat("#.##");

	public Resultat(){

	}
	
	public Resultat(String algo, int fitness, double tempsEcourle, int nbIteration) {
		this.algo = algo;
		this.fitness = fitness;
		this.tempsEcourle = tempsEcourle;
		this.nbIteration = nbIteration;

	}

	public int getNbIteration() {
		return nbIteration;
	}

	public void setNbIteration(int nbIteration) {
		this.nbIteration = nbIteration;
	}

	public String getAlgo() {
		return algo;
	}
	public void setAlgo(String algo) {
		this.algo = algo;
	}
	public int getFitness() {
		return fitness;
	}
	public void setFitness(int fitness) {
		this.fitness = fitness;
	}
	public double getTempsEcourle() {
		return tempsEcourle;
	}
	public void setTempsEcourle(double tempsEcourle) {
		this.tempsEcourle = tempsEcourle;
	}

	@Override
	public String toString() {
		return "Resultat [algo=" + algo + ", fitness=" + fitness + ", tempsEcourle=" + df2.format(tempsEcourle) + "sec, nbIteration="
				+ nbIteration ;
	}

	
	
}
