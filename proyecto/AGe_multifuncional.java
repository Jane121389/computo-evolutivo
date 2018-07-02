import java.io.*;
import java.util.*;
import java.io.File;
import java.io.FileWriter;
class AGe_multifuncional<T extends AGe_multifuncional.Modelo>
{
    T modelo;
    int longitud;
    int individuos;
    int poblacion[][];
    double pm, pc;
    int n_funciones;
    int [] pob;
    String cadena1, cadena2;
    public AGe_multifuncional(T modelo, int longitud, int individuos, double pc, double pm, int n_funciones, String cadena1, String cadena2)
    {
        this.modelo    = modelo;
        this.longitud  =longitud;
        this.individuos=individuos;
        this.cadena1   =cadena1;
        this.cadena2   =cadena2;
        //this.n_funciones=n_funciones;
        this.n_funciones=2;
        this.pc         =pc;
        this.pm         =pm;
        poblacion       =new int [individuos][longitud];//Matriz de la población de individuos
        pob             =new int[individuos];
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

    double [] dominancia_goldberg(double [][] eval_f)
    {
        int       i=0, j, k, l, nivel=1;
        double    suma_t=0, menor=1000;
        double [] niveles= new double[individuos];
        double [] ord    =new double[individuos];
        do {
            for (j=0; j < individuos; j++)
                if (niveles[j] != -1)
                    niveles[j]=0;
            for (j=0; j < individuos; j++)
                for (k=j + 1; k < individuos; k++)
                    if (niveles[j] != -1 && niveles[k] != -1)
                        if ((eval_f[k][0] > eval_f[j][0] && eval_f[k][1] <= eval_f[j][1]) || (eval_f[k][0] >= eval_f[j][0] && eval_f[k][1] < eval_f[j][1]))


                            //System.out.print("nivel k"+niveles[k]);
                            niveles[k]=niveles[k] + 1;
                        else if ((eval_f[j][0] > eval_f[k][0] && eval_f[j][1] <= eval_f[k][1]) || (eval_f[j][0] >= eval_f[k][0] && eval_f[j][1] < eval_f[k][1]))
                            //System.out.print("nivel j "+niveles[j]);
                            niveles[j]=niveles[j] + 1;

            menor=1000;
            for (j=0; j < individuos; j++)
                if (niveles[j] != -1 && niveles[j] < menor)
                    menor=niveles[j];

            for (j=0; j < individuos; j++)
                if (niveles[j] == menor)
                {
                    pob[i]    =j;
                    ord[i]    =nivel;
                    niveles[j]=-1;
                    i++;
                }
            nivel++;
        } while (i < individuos);
        for (j=0; j < individuos; j++)
            suma_t=suma_t + ord[j];
        for (j=0; j < individuos; j++)
            ord[j]=ord[j] / suma_t;
        return ord;
    }

    void guarda_archivo(double [][] eval_f)
    {
        try
        {
            //Crear un objeto File se encarga de crear o abrir acceso a un archivo que se especifica en su constructor
            File archivo=new File("grafica_dominancia_f.txt");

            //Crear objeto FileWriter que sera el que nos ayude a escribir sobre archivo
            FileWriter escribir=new FileWriter(archivo, true);
            int        i=0, j=0;
            String     puntos="";
            for (j=0; j < individuos; j++)
            {
                puntos=eval_f[j][0] + " " + eval_f[j][1];
                escribir.write(puntos + "\n");
            }
            escribir.close();
        }
        //Si existe un problema al escribir cae aqui
        catch (Exception e)
        {
            System.out.println("Error al escribir" + e);
        }
    }

    double [] dominancia_fonseca(double [][] eval_f)
    {
        int       i=0, j, k, l, menor=100, aux2=0;
        double    suma_t=0, aux=0;
        double [] niveles= new double[individuos];
        double [] ord    =new double[individuos];
        for (j=0; j < individuos; j++)
            niveles[j]=1;
        for (j=0; j < individuos; j++)
            for (k=j + 1; k < individuos; k++)
                if ((eval_f[k][0] > eval_f[j][0] && eval_f[k][1] <= eval_f[j][1]) || (eval_f[k][0] >= eval_f[j][0] && eval_f[k][1] < eval_f[j][1]))
                    niveles[k]=niveles[k] + 1;
                else if ((eval_f[j][0] > eval_f[k][0] && eval_f[j][1] <= eval_f[k][1]) || (eval_f[j][0] >= eval_f[k][0] && eval_f[j][1] < eval_f[k][1]))
                    niveles[j]=niveles[j] + 1;
        for (j=0; j < individuos; j++)
        {
            ord[j]=niveles[j];
            suma_t=suma_t + ord[j];
        }
        for (j=0; j < individuos; j++)
            ord[j]=ord[j] / suma_t;
            //System.out.print(" "+ord[j]);
        return ord;
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
                    //nueva_poblacion=copia(nueva_poblacion,poblacion[pob[k]],i);
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
        int    i=0, j=0, aux=0;
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
        int    i=0, j=0, cont=0;
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

    double convergencia2(int [][] nueva_poblacion)
    {
        int       i=0;
        double [] ord=new double[individuos];
        double    val=0, su=0;
        ord=dominancia_fonseca(modelo.aptitud(nueva_poblacion, n_funciones, cadena1, cadena2));
        //ord=dominancia_goldberg(modelo.aptitud(nueva_poblacion,n_funciones,cadena1,cadena2));
        val=ord[i];
        for (i=1; i < individuos; i++)
            if (val == ord[i])
                su++;
        //System.out.print(" su:"+(su/individuos));
        return su / individuos;
    }

    int aplica_ag()
    {
        double [] evaluacion=new double[individuos];
        int [][]  nueva_poblacion=new int[individuos][longitud];
        double    converge=0;
        double    mayor=0, mejor=0;
        int       i=0, cont=0;
        int       pos=0, j=0, conta=0, cont2=0;
        String    puntos="";
        double    media =0;
        poblacion=genera_poblacion();
        try
        {
            //Crear un objeto File se encarga de crear o abrir acceso a un archivo que se especifica en su constructor
            File archivo=new File("hola.txt");

            //Crear objeto FileWriter que sera el que nos ayude a escribir sobre archivo
            FileWriter escribir=new FileWriter(archivo, true);

            evaluacion=dominancia_goldberg(modelo.aptitud(poblacion, n_funciones, cadena1, cadena2));
            //evaluacion=dominancia_fonseca(modelo.aptitud(poblacion,n_funciones,cadena1,cadena2));
            while (cont < 50)
            {
                media          =0;
                mayor          =0;
                nueva_poblacion=selecciona(evaluacion);
                nueva_poblacion=cruza(nueva_poblacion);
                nueva_poblacion=mutar(nueva_poblacion);
                converge       =convergencia2(nueva_poblacion);
                poblacion      =nueva_poblacion;
                cont++;
                evaluacion=dominancia_fonseca(modelo.aptitud(poblacion, n_funciones, cadena1, cadena2));
                //evaluacion=dominancia_goldberg(modelo.aptitud(poblacion,n_funciones,cadena1,cadena2));
                for (i=0; i < individuos; i++)
                {
                    if (evaluacion[i] > mayor)
                    {
                        mayor=evaluacion[i];
                        pos  =i;
                    }
                    media=media + evaluacion[i];
                    //System.out.print(evaluacion[i]+"   ");
                }
                media =media / individuos;
                puntos=cont + " " + media + " " + mayor;
                escribir.write(puntos + "\n");
                //System.out.println("*****************conv:"+converge);
            }

            modelo.finales(poblacion, pob[pos], cadena1, cadena2);
            escribir.close();
            guarda_archivo(modelo.aptitud(poblacion, n_funciones, cadena1, cadena2));
            //dominancia_fonseca_gu(modelo.aptitud(poblacion,n_funciones,cadena1,cadena2));
        }
        //Si existe un problema al escribir cae aqui
        catch (Exception e)
        {
            System.out.println("Error al escribir" + e);
        }
        return cont;
    }

    double [] dominancia_fonseca_gu(double [][] eval_f)
    {
        int       i=0, j, k, l, menor=100, aux2=0;
        double    suma_t=0, aux=0;
        double [] niveles= new double[individuos];
        double [] ord    =new double[individuos];
        for (j=0; j < individuos; j++)
            niveles[j]=1;
        for (j=0; j < individuos; j++)
            for (k=j + 1; k < individuos; k++)
                if ((eval_f[k][0] < eval_f[j][0] && eval_f[k][1] >= eval_f[j][1]) || (eval_f[k][0] <= eval_f[j][0] && eval_f[k][1] > eval_f[j][1]))
                    niveles[k]=niveles[k] + 1;
                else if ((eval_f[j][0] < eval_f[k][0] && eval_f[j][1] >= eval_f[k][1]) || (eval_f[j][0] <= eval_f[k][0] && eval_f[j][1] > eval_f[k][1]))
                    niveles[j]=niveles[j] + 1;
        try
        {
            //Crear un objeto File se encarga de crear o abrir acceso a un archivo que se especifica en su constructor
            File archivo=new File("grafica_dominancia_f.txt");

            //Crear objeto FileWriter que sera el que nos ayude a escribir sobre archivo
            FileWriter escribir=new FileWriter(archivo, true);
            String     puntos  ="";
            for (j=0; j < individuos; j++)
                if (niveles[j] == 1)
                {
                    //System.out.print(niveles[j]+" ");
                    puntos=eval_f[j][0] + " " + eval_f[j][1];
                    escribir.write(puntos + "\n");
                }
            for (j=0; j < individuos; j++)
                ord[j]=ord[j] / suma_t;
            escribir.close();
        }
        //Si existe un problema al escribir cae aqui
        catch (Exception e)
        {
            System.out.println("Error al escribir" + e);
        }

        return ord;
    }

    interface Modelo
    {
        double[][] aptitud(int[][] poblacion, int n_funciones, String cadena1, String cadena2);

        void finales(int [][] poblacion, int ind, String cadena1, String cadena2);
    }
}
