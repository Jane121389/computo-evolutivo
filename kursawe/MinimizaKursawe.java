import java.io.*;
import java.util.*;
import java.text.DecimalFormat;
public class MinimizaKursawe implements AGe_multifuncional.Modelo
{
    int [][][] px;
    int[][] poblacion;
    int [] m_x;
    int n;
    double [] a_x;
    double [] b_x;
    int n_funciones;
    @Override
    public double[][] aptitud(int[][] poblacion, int n_funciones)
    {
        double [] x=new double[n];
        int       i, k;
        this.n_funciones=n_funciones;
        double     total=0, minimo=0, total2=0, minimo2=0;
        double[][] aptitudes = new double[poblacion.length][n_funciones];
        this.poblacion =poblacion;
        valores();
        for (i=0; i < poblacion.length; i++) {
            total =0;
            total2=0;
            for (k=0; k < n; k++)
                x[k]=decodifica(px, a_x[k], b_x[k], m_x[k], i, k);
            for (k=0; k < n - 1; k=k + 1)
                total=total + (-10 * Math.exp(-.2 * Math.sqrt(Math.pow(x[k], 2) + Math.pow(x[k + 1], 2))));
            for (k=0; k < n; k++)
                total2=total2 + (Math.pow(Math.abs(x[k]), 0.8) + 5 * Math.sin(Math.pow(x[k], 3)));
            //System.out.print(" f1:"+total+ "f2:"+total2);
            aptitudes[i][0]=total;
            aptitudes[i][1]=total2;
        }
        return aptitudes;
    }

    @Override
    public void finales(int [][] poblacion, int ind)
    {
        int       k =0;
        double    total=0, total2=0;
        double [] x =new double[n];
        this.poblacion=poblacion;
        valores();
        DecimalFormat df = new DecimalFormat("#.####");
        for (k=0; k < n; k++)
        {
            x[k]=decodifica(px, a_x[k], b_x[k], m_x[k], ind, k);
            System.out.print(" x" + k + ":" + df.format(x[k]));
        }
        for (k=0; k < n - 1; k=k + 1)
            total=total + (-10 * Math.exp(-.2 * Math.sqrt(Math.pow(x[k], 2) + Math.pow(x[k + 1], 2))));
        for (k=0; k < n; k++)
            total2=total2 + (Math.pow(Math.abs(x[k]), 0.8) + 5 * Math.sin(Math.pow(x[k], 3)));
        System.out.println("");
        System.out.print(" f1:" + df.format(total) + "f2:" + df.format(total2));
    }

    int num_codifica(double a, double b, int presicion)
    {
        int    longitud_total=0;
        double longitud      =0;
        longitud      =b - a;
        presicion     =(int)Math.pow(10, presicion);
        longitud      =longitud * presicion;
        longitud_total=(int)(Math.log(longitud) / Math.log(2));
        return longitud_total + 1;
    }

    int encuentra_longitud()
    {
        Scanner sc =new Scanner(System.in);
        int     presicion=0, i=0, total_m=0;
        System.out.println("Escriba la presición:");
        presicion=sc.nextInt();
        System.out.println("Escriba número de elementos n:");
        n  =sc.nextInt();
        a_x=new double[n];
        b_x=new double[n];
        m_x=new int[n];
        for (i=0; i < n; i++)
        {
            System.out.println("Escriba el rango minimo de x" + i + ":");
            a_x[i]=sc.nextDouble();
            System.out.println("Escriba el rango maximo de x" + i + ":");
            b_x[i] =sc.nextDouble();
            m_x[i] =num_codifica(a_x[i], b_x[i], presicion);
            total_m=total_m + m_x[i];
        }
        return total_m;
    }

    void valores()
    {
        int    j=0, i=0, k=0, s=0;
        int    presicion;
        int    k_ant  =0;
        double funcion=0;
        px=new int[n][poblacion.length][m_x[0]];
        for (k=0; k < n; k++)
        {
            for (i=0; i < poblacion.length; i++)
            {
                for (j=k_ant; j < (k_ant + m_x[k]); j++)
                {
                    px[k][i][s]=poblacion[i][j];
                    s++;
                }
                s=0;
            }
            k_ant=k_ant + m_x[k];
        }
    }

    double decodifica(int [][][] poblacion, double a, double b, int m, int ind, int k)
    {
        double x=0;
        x=a + ((convierteANumero(poblacion[k][ind])) * ((b - a) / (Math.pow(2, m) - 1)));
        return x;
    }

    int convierteANumero(int[] individuo)
    {
        int potencia = 1;
        int total    = 0;
        for (int i=individuo.length - 1; i >= 0; i--) {
            total   += potencia * individuo[i];
            potencia*=2;
        }
        return total;
    }
}
