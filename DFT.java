package sample;

/**
 * Created by abedaigorou on 16/02/25.
 */
import static java.lang.StrictMath.*;

public class DFT {
    private int m = 0;
    private byte[] F;
    public final static int f = 400;

    public DFT(int m, byte[] F)
    {
        this.m=m;
        this.F=F;
    }

    public double an(int n)
    {
        double an=0;
        for(int k=0;k<m;k++)
            an += F[k] * cos(n * (2 * PI * k / m));
            //if(an/m>0.1)
            System.out.println("an(" + n+ ")" + an / m);

        return (an/m);
    }

    public double bn(int n)
    {
        double bn=0;
        for(int k=0;k<m;k++)
            bn += -F[k] * sin(n * (2 * PI * k / m));
            //if(bn/m>0.1)
            System.out.println("bn(" + n + ")" + bn / m);

        return (bn/m);
    }

    public static double F(double x)
    {
        double fx=sin(2*PI*f*x);

        //double fx=x;
        return (fx);
    }
}
