import java.io.*;
import java.util.*;
import java.text.DecimalFormat;
public class MinimizaCadennas implements AGe_multifuncional.Modelo
{
    int [][][] px;
    int[][] poblacion;
    int [] m_x;
    int n;
    double [] a_x;
    double [] b_x;
    int n_funciones;
    @Override
    public double[][] aptitud(int[][] poblacion, int n_funciones, String cadena1, String cadena2)
    {
        double [] x=new double[n];
        int       i, k, temp1=0, temp2=0, div=0, tmin=37, tmax=60;
        String    subcadena="";
        this.n_funciones=n_funciones;
        double     total=0, minimo=0, total2=0, minimo2=0, factor_restriccion=0, factor_restriccion2=0;
        double[][] aptitudes = new double[poblacion.length][n_funciones];
        this.poblacion=poblacion;
        valores();
        for (i=0; i < poblacion.length; i++) {
            total =0;
            total2=0;
            for (k=0; k < n; k++)
                x[k]=decodifica(px, a_x[k], b_x[k], m_x[k], i, k);
            k                  =0;//solo hay una variable
            subcadena          =encuentra_sub(x[k], cadena1.length(), cadena1);
            temp1              =temperatura(subcadena);
            temp2              =compara_conT(subcadena, cadena2);
            aptitudes[i][0]    =temp1;
            aptitudes[i][1]    =temp2;
            factor_restriccion =tmax - temp1;
            factor_restriccion2=subcadena.length() - 23;
            if (factor_restriccion > 0)
                aptitudes[i][0]=temp1 - (0.2 * Math.pow(factor_restriccion, 2));
            if (factor_restriccion2 > 0)
                aptitudes[i][0]=aptitudes[i][0] - (0.7 * Math.pow(factor_restriccion2, 2));
            factor_restriccion=temp2 - tmin;
            if (factor_restriccion > 0)
                aptitudes[i][1]=temp2 + 0.2 * Math.pow(factor_restriccion, 2);
            if (factor_restriccion2 > 0)
                aptitudes[i][1]=aptitudes[i][1] + (0.7 * Math.pow(factor_restriccion2, 2));
            //factor_restriccion=Math.abs(tmin-temp2);
            //System.out.println("subcadena:"+subcadena+"temp1:"+temp1+"temp2:"+temp2+"ap0:"+aptitudes[i][0]+"ap1:"+aptitudes[i][1]);
            /*con restricciones
               if(temp1<tmax)
                factor_restriccion=tmax-temp1;
               else
                factor_restriccion=0;
               if(temp1-factor_restriccion>0)
                aptitudes[i][0]=temp1-factor_restriccion;
               else
                aptitudes[i][0]= 0;
               if(temp2>tmin)
                factor_restriccion=temp2-tmin;
               else
                factor_restriccion=0;
               if(temp1-factor_restriccion>0)
                aptitudes[i][1]=temp2+factor_restriccion;
               else
                aptitudes[i][1]= 0;*/
        }
        return aptitudes;
    }

    @Override
    public void finales(int [][] poblacion, int ind, String cadena1, String cadena2)
    {
        int       k=0;
        double    total=0, total2=0;
        double [] x=new double[n];
        this.poblacion=poblacion;
        valores();
        DecimalFormat df = new DecimalFormat("#.#");
        System.out.println(" n:" + n);
        for (k=0; k < n; k++)
            x[k]=decodifica(px, a_x[k], b_x[k], m_x[k], ind, k);
            //System.out.print(" x"+k+":"+df.format(x[k])+ "    ");
        //System.out.println("X"+x[0]);
        String subcadena=encuentra_sub(x[0], cadena1.length(), cadena1);
        System.out.println(" Subcadena:" + subcadena + " f1:" + temperatura(subcadena) + "f2:" + compara_conT(subcadena, cadena2) + " oligo de interfencia " + complemento(subcadena));
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

    int encuentra_longitud(int elementos, double min, double max)
    {
        Scanner sc=new Scanner(System.in);
        int     presicion=0, i=0, total_m=0;
        presicion=1;
        n        =elementos;//1
        a_x      =new double[n];
        b_x      =new double[n];
        m_x      =new int[n];
        for (i=0; i < n; i++)
        {
            a_x[i] =min;
            b_x[i] =max;
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

    public String encuentra_sub(double numero, int longitud, String cadena)
    {
        int i      =0;
        int num_des=longitud--;
        int tamaño =1;
        int indice =0;
        for (i=num_des; i < numero; i=i + (longitud--))
            //System.out.print(" long  "+longitud+ "  i "+i );
            tamaño++;
        indice=(int)(numero - (i - longitud - 1));
        //System.out.print(" tamaño: "+tamaño+ " indice:"+indice);
        //System.out.print(" subcadena:"+cadena.substring(indice,(indice+tamaño)));
        return cadena.substring(indice, (indice + tamaño - 1));
    }

    public String complemento(String subcadena)
    {
        int    i    =0;
        String nueva="";
        char   letra=' ';
        for (i=0; i < subcadena.length(); i++)
        {
            letra=subcadena.charAt(i);
            switch (letra)
            {
                case 'A'://la letra A cambia por U
                    nueva=nueva + 'T';
                    break;
                case 'C'://la letra C cambia por G
                    nueva=nueva + 'G';
                    break;
                case 'G'://la letra G cambia por C
                    nueva=nueva + 'C';
                    break;
                case 'T'://la letra T cambia por A
                    nueva=nueva + 'A';
                    break;
            }
        }
        return nueva;
    }

    public int compara_conT(String subcadena, String cadena)
    {
        int    longitud=subcadena.length();
        int [] ind_sub=new int[longitud];
        int [] peso=new int[longitud];
        int    i=0, s=0, j=0, mayor=0, mejor_i=0;
        for (i=0; i > (-longitud); i--)
            ind_sub[s]=i;
            //System.out.print(" "+ind_sub[s]);
        for (i=0; i < cadena.length(); i++)
            for (j=0; j < longitud; j++)
            {
                if (ind_sub[j] >= 0)
                    if (cadena.charAt(i) == subcadena.charAt(ind_sub[j]))
                        if (cadena.charAt(i) == 'A' || cadena.charAt(i) == 'T')
                            peso[j]=peso[j] + 2;
                        else if (cadena.charAt(i) == 'C' || cadena.charAt(i) == 'G')
                            peso[j]=peso[j] + 4;
                ind_sub[j]=ind_sub[j] + 1;
                if (ind_sub[j] >= longitud)
                {
                    if (peso[j] > mayor)
                    {
                        mayor  =peso[j];
                        mejor_i=i;
                    }
                    peso[j]   =0;
                    ind_sub[j]=0;
                }
            }
        //	System.out.print("índice:"+(mejor_i-longitud+1 )+ " temperatura:"+mayor);
        return mayor;
    }

    /*Devuelve la temperatura de disociación*/
    public int temperatura(String subcadena)
    {
        int  i, tm=0;
        char letra   =' ';
        int  longitud=subcadena.length();
        for (i=0; i < longitud; i++)
        {
            letra=subcadena.charAt(i);
            switch (letra)
            {
                case 'A'://la letra A  sumara 2 a la temperatura discociación
                    tm=tm + 2;
                    break;
                case 'C'://la letra C sumara 4 a la temperatura discociación
                    tm=tm + 4;
                    break;
                case 'G'://la letra G sumara 4 a la temperatura discociación
                    tm=tm + 4;
                    break;
                case 'T'://la letra T sumara 2 a la temperatura discociación
                    tm=tm + 2;
                    break;
            }
        }
        return tm;
    }
}
