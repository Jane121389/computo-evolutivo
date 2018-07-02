import java.io.*;
import java.util.*;
import java.io.File;
import java.io.FileWriter;
class AGe<T extends AGe.Modelo>
{
    T modelo;
    int longitud;
    int individuos;
    int poblacion[][];
    double pm, pc;
    public AGe(T modelo, int longitud, int individuos, double pc, double pm)
    {
        this.modelo    = modelo;
        this.longitud  =longitud;
        this.individuos=individuos;
        this.pc        =pc;
        this.pm        =pm;
        poblacion      =new int [individuos][longitud]; //Matriz de la población de individuos
    }

    int [][] genera_poblacion()
    {
        int i=0, j=0;    //contadores de la matriz i= filas,j=columnas
        for (j=0; j < longitud; j++)
            for (i=0; i < individuos; i++)
                if (Math.random() < .5)
                    poblacion[i][j]=0;
                else
                    poblacion[i][j]=1;
        //imprime(poblacion);
        return poblacion;
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

    void imprime(int[] matriz)
    {
        for (int j=0; j < matriz.length; j++)
            System.out.print(matriz[j] + " ");
        System.out.println("");
    }

    void imprime(double[] matriz)
    {
        for (int j=0; j < matriz.length; j++)
            System.out.print(matriz[j] + " ");
        System.out.println("");
    }

    void imprime(int[][] matriz)
    {
        for (int i=0; i < matriz.length; i++) {
            System.out.println(" ");
            for (int j=0; j < matriz[0].length; j++)
                System.out.print(matriz[i][j] + " ");
        }
    }

    double [] evalua()
    {
        double [] aptitudes       = new double[individuos];  //Arreglo de las aptitudes
        double [] aptitud_relativa= new double [individuos]; //Arreglo de las aptitudes relativas
        double    aptitud_total   =0;
        int       i;
        aptitudes=modelo.aptitud(poblacion);
        //imprime(aptitudes);
        for (i=0; i < individuos; i++)
            aptitud_total=aptitud_total + aptitudes[i];
        for (i=0; i < individuos; i++)
            aptitud_relativa[i]=aptitudes[i] / aptitud_total;
        return aptitud_relativa;
    }

    int [][] selecciona(double [] evaluacion)
    {
        int [][]  nueva_poblacion=new int [individuos][longitud];
        int       n=0, i=0, k=0, individuo;
        int       j=0, cont=0, cont2=0;
        int []    mejor=new int[individuos];
        double    mayor=0, aleatorio= 0;
        double [] escoge=new double[individuos];

        for (i=0; i < individuos; i++)
        {
            if (i > 0)
                escoge[i]=evaluacion[i] + escoge[i - 1];
            else
                escoge[i]=evaluacion[i];
        }
        for (i=0; i < individuos; i++)
        {
            aleatorio=Math.random();
            for (k=0; k < individuos; k++)
                if (aleatorio <= escoge[k])
                {
                    nueva_poblacion=copia(nueva_poblacion, poblacion[k], i);
                    //modelo.finales(poblacion,k);
                    break;
                }
        }
        //System.out.print(" pobl_mayores:"+cont2);
        return nueva_poblacion;
    }

    int [][] copia(int [][] nueva_poblacion, int [] individuo, int fila)
    {
        int i=0;
        for (i=0; i < longitud; i++)
            nueva_poblacion[fila][i]=individuo[i];
        return nueva_poblacion;
    }

    int [][] cruza(int [][] nueva_poblacion)
    {
        double aleatorio=0;
        int    i =0, j=0, aux=0;
        Random rnd = new Random();
        for (i=0; i < individuos; i++)
        {
            if (Math.random() < pc)
            {
                aleatorio=rnd.nextInt(longitud - 1);
                for (j=(int)aleatorio; j < longitud; j++)
                {
                    aux=nueva_poblacion[i][j];
                    if (i + 1 < individuos)
                    {
                        nueva_poblacion[i][j]    =nueva_poblacion[i + 1][j];
                        nueva_poblacion[i + 1][j]=aux;
                    }
                }
            }
            i++;
        }
        return nueva_poblacion;
    }

    int [][] mutar(int [][] nueva_poblacion)
    {
        int i=0, j=0, cont=0;
        for (i=0; i < individuos; i++)
            for (j=0; j < longitud; j++)
                if (Math.random() < pm)
                {
                    if (nueva_poblacion[i][j] == 0)
                        nueva_poblacion[i][j]=1;
                    else
                        nueva_poblacion[i][j]=0;
                    cont++;
                }
        //System.out.print("muta:"+cont);
        return nueva_poblacion;
    }

    double convergencia(int [][] nueva_poblacion)
    {
        int    i =0, j=0, cont=0;
        double total=0;
        for (j=0; j < longitud; j++)
        {
            for (i=0; i < individuos; i++)
                if (nueva_poblacion[i][j] == 1)
                    cont++;
            if (cont < (individuos / 2))
                total=total + (individuos - cont);
            else
                total=total + cont;
            cont=0;
        }
        total=total / (longitud * individuos);
        return total;
    }

    int aplica_ag()
    {
        double [] evaluacion =new double[individuos];
        int [][]  nueva_poblacion=new int[individuos][longitud];
        double    converge=0;
        double    mayor =0, mejor=0;
        int       i =0, cont=0;
        int       pos =0, j=0, conta=0, cont2=0;
        String    puntos   ="";
        double    media    =0;
        double [] aptitudes=new double[individuos];
        poblacion =genera_poblacion();
        try
        {
            //Crear un objeto File se encarga de crear o abrir acceso a un archivo que se especifica en su constructor
            File archivo=new File("hola.txt");

            //Crear objeto FileWriter que sera el que nos ayude a escribir sobre archivo
            FileWriter escribir=new FileWriter(archivo, true);
            while (converge < .975)
            {
                media          =0;
                mayor          =0;
                evaluacion     =evalua();
                nueva_poblacion=selecciona(evaluacion);
                nueva_poblacion=cruza(nueva_poblacion);
                nueva_poblacion=mutar(nueva_poblacion);
                converge       =convergencia(nueva_poblacion);
                poblacion      =nueva_poblacion;
                cont++;
                aptitudes=modelo.aptitud(nueva_poblacion);

                for (i=0; i < individuos; i++)
                {
                    if (aptitudes[i] > mayor)
                        mayor=aptitudes[i];

                    media=media + aptitudes[i];
                }
                media =media / individuos;
                puntos=cont + " " + media + " " + mayor;
                escribir.write(puntos + "\n");
                //System.out.println("conv:"+converge);
            }
            System.out.print("conv:" + converge + "Generaciones:" + cont + ",");
            evaluacion=evalua();
            for (i=0; i < individuos; i++)
                if (evaluacion[i] > mayor)
                {
                    mayor=evaluacion[i];
                    pos  =i;
                }
            System.out.println("");
            modelo.finales(poblacion, pos);
            escribir.close();
        }
        //Si existe un problema al escribir cae aqui
        catch (Exception e)
        {
            System.out.println("Error al escribir");
        }
        return cont;
    }

    interface Modelo
    {
        double[] aptitud(int[][] poblacion);

        void finales(int [][] poblacion, int ind);
    }
}
