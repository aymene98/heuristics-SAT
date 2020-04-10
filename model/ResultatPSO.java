package model;

public class ResultatPSO extends Resultat {
    private double c1,c2,w;

    public ResultatPSO(int fitness, double tempsEcourle, int nbIteration, double c1,
                           double c2, double w) {
        super("PSO",fitness,tempsEcourle,nbIteration);
        this.c1 = c1;
        this.c2 = c2;
        this.w = w;
    }

    @Override
    public String toString() {
        return super.toString() + ", c1=" + this.c1 + ", c2=" + this.c2 + ", w=" + this.w + "]";
    }

}
