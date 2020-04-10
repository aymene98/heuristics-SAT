package model;

public class ResultatGenetic extends Resultat {
    private double crossRate,mutationRate;
    private int finalPopulationSize;

    public ResultatGenetic(int fitness, double tempsEcourle, int nbIteration, double crossRate,
                    double mutationRate,int finalPopulationSize) {
        super("Genetic",fitness,tempsEcourle,nbIteration);
        this.crossRate = crossRate;
        this.mutationRate = mutationRate;
        this.finalPopulationSize = finalPopulationSize;
    }

    @Override
    public String toString() {
        return super.toString() + ", crossRate=" + crossRate + ", mutationRate=" + mutationRate + ", final population size=" + this.finalPopulationSize + "]";
    }

}
