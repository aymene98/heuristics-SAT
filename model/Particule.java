package model;

public class Particule implements Comparable<Particule>{
	private Solution current, best;
	private int velocity = 0;
	
	public Particule(Solution current, Solution best) {
		this.current = current;
		this.best = best;
	}
	
	public Particule(Solution current) {
		int temp[] = new int[current.getSolution().length];
		for (int i = 0; i < temp.length; i++) {
			temp[i] = current.getSolution()[i];
		}
		this.current = new Solution(temp.length);
		this.current.setSolution(temp);
		this.best = new Solution(temp.length);
		this.best.setSolution(temp);
	}
	public Particule(Particule p) {
		current = p.getCurrent();
		best = p.getBest();
		this.velocity = p.getVelocity();
	}

	public Solution getCurrent() {
		return current;
	}

	public void setCurrent(Solution current) {
		this.current = current;
	}

	public Solution getBest() {
		return best;
	}

	public void setBest(Solution best) {
		this.best = best;
	}

	@Override
	public int compareTo(Particule o) {
		return this.getBest().compareTo(o.getBest());
	}

	@Override
	public String toString() {
		return "Particule [current=" + current + ", best=" + best + ", velocity=" + velocity + "]";
	}

	public int getVelocity() {
		return velocity;
	}

	public void setVelocity(int velocity) {
		this.velocity = velocity;
	}
	
	public void setFitnessCurrent(int fitness){
		this.current.setFitness(fitness);
	}
	
	public void setFitnessBest(int fitness){
		this.best.setFitness(fitness);
	}
	
	
	
}
