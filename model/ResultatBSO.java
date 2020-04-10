package model;

public class ResultatBSO extends Resultat {

    private int bees, flips,numberOfLocalSearchIterations,nbChances;

    public ResultatBSO(int fitness, double tempsEcourle, int nbIteration,int bees, int flips, int numberOfLocalSearchIterations, int nbChances) {
        super("BSO",fitness,tempsEcourle,nbIteration);
        this.bees = bees;
        this.flips = flips;
        this.numberOfLocalSearchIterations = numberOfLocalSearchIterations;
        this.nbChances = nbChances;
    }

    @Override
    public String toString() {
        return super.toString() + ", Number of bees=" + this.bees + ", Flips=" + this.flips + ", Number of local search iterations=" + this.numberOfLocalSearchIterations + ", Number of chances to update Sref=" + this.nbChances + "]";
    }
}
