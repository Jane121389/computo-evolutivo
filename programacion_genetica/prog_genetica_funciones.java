import java.io.*;
import java.util.*;
import java.io.File;
import java.io.FileWriter;
public class Programa_genetico_funciones
{
    int profundidad=0;
    int n_funciones;
    int [] elem_funciones;
    int n_elementos;
    char [] funciones;
    double [] elementos;
    int poblacion;
    double [] comparar;
    Nodo_f arboles[];
    int nodos=0;
    boolean continuar;
    double [] re_funcion;
    Nodo_f aux       =null;
    int indiceGlobal = 0;
    int individuo    =0;
    int ver_elem[];
    int cont=0;
    Nodo_f mejor;
    double mejores=0;
    static enum Tipos
    {
        IZQUIERDO, DERECHO, RAIZ
    };

    public void inicia(int n_funciones, int [] elem_funciones, int n_elementos, int profundidad, char [] funciones, double [] elementos, int poblacion, double [] comparar, double [] re_funcion)
    {
        this.n_funciones   =n_funciones;
        this.elem_funciones=elem_funciones;
        this.n_elementos   =n_elementos;
        this.profundidad   =profundidad;
        this.funciones     =funciones;
        this.elementos     =elementos;
        this.poblacion     =poblacion;
        this.comparar      =comparar;
        this.re_funcion    =re_funcion;
        ver_elem           =new int[n_elementos];
        for (int i=0; i < n_elementos; i++)
            ver_elem[i]=0;
        mejor=forma_arbol(0, profundidad);
    }

    public Nodo_f forma_arbol(int e, int profundidad)
    {
        Random rnd      = new Random();
        double aleatorio=0;
        if (e == 0)
            aleatorio=rnd.nextInt(n_funciones);
        else
            aleatorio=rnd.nextInt(n_funciones + n_elementos);
        if (aleatorio < n_funciones && e < profundidad)
        {
            int    num  =elem_funciones[(int)aleatorio];
            Nodo_f nodin=new Nodo_f((int)aleatorio);
            for (int i=0; i < num; i++)
            {
                if (i % 2 == 0)
                    nodin.izquierdo=forma_arbol(e + 1, profundidad);
                else
                    nodin.derecho=forma_arbol(e + 1, profundidad);
            }
            return nodin;
        }
        else
        {
            aleatorio=rnd.nextInt(n_elementos);
            e++;
            if (aleatorio == 1)
                aleatorio=rnd.nextInt(10);
            Nodo_f nodo=new Nodo_f(aleatorio + n_funciones);
            return nodo;
        }
    }

    public void recorre2(Nodo_f nodo)
    {
        double num=0;
        if (nodo != null)
        {
            System.out.print("(");
            num=nodo.valor;
            if (num >= n_funciones)
            {
                num=num - n_funciones;
                if (num == 0)
                    System.out.print("x");
                else
                {
                    if (num > 5)
                        System.out.print(num - 10);
                    else
                        System.out.print(num);
                }
            }
            else
                System.out.print(funciones[(int)num]);
            recorre2(nodo.izquierdo);
            recorre2(nodo.derecho);
            System.out.print(")");
        }
    }

    public double evalua1(Nodo_f nodo, int fila)
    {
        if (nodo != null)
        {
            if (nodo.valor < n_funciones)
                return opera((int)nodo.valor, evalua1(nodo.izquierdo, fila), evalua1(nodo.derecho, fila));
            else if (nodo.valor - n_funciones == 0)
                return comparar[fila];
            else
            {
                if (nodo.valor - n_funciones > 5)
                    return nodo.valor - n_funciones - 10;
                else
                    return nodo.valor - n_funciones;
            }
        }
        return 0;
    }

    public double [] evalua_general()
    {
        double    resultados     =0;
        double    contador       =0;
        int       num_nodos      =0;
        double    sum            =0;
        boolean   total          =true;
        double [] resultado_total=new double[poblacion];
        for (int i=0; i < poblacion; i++)
        {
            contador=0;
            for (int j=0; j < re_funcion.length; j++)
            {
                resultados=evalua1(arboles[i], j);
                //System.out.print(" resta:"+Math.abs(Math.abs(resultados)-Math.abs(re_funcion[j])));
                if (resultados != re_funcion[j])
                    contador=contador + (Math.abs(resultados - re_funcion[j]));
            }
            num_nodos=numero_nodos(arboles[i]);
            //contador=contador+(numero_nodos(arboles[i])/1000);
            if (num_nodos <= 15)
                resultado_total[i]=.3 * num_nodos / 15;
            else if (num_nodos <= 85)
                resultado_total[i]=.3 * (1.17 - num_nodos / 85);
            else
                resultado_total[i]=0;
            contador=contador / re_funcion.length;
            if (contador <= 100)
                resultado_total[i]=resultado_total[i] + (.7 * (1 - contador / 100));
            else
                resultado_total[i]=resultado_total[i] + 0;
            //System.out.print("resultado :"+resultados_total[i]+ " contador "+contador+ " nodos:"+num_nodos);
            //resultados_total[i]=1/((.3*contador)+(.7*num_nodos));
        }
        return resultado_total;
    }

    public Nodo_f [] arma_poblacion()
    {
        int i=0;
        arboles=new Nodo_f[poblacion];
        for (i=0; i < poblacion; i++)
            arboles[i]=forma_arbol(0, profundidad);
        return arboles;
    }

    public Nodo_f[] selecciona(double [] evaluacion)
    {
        double    aleatorio, sum=0, peor=1000;
        int       i=0, k=0, peor_i=0, nodo_i=0;
        double [] evaluacion2    =new double[poblacion];
        double [] escoge         =new double[poblacion];
        Nodo_f [] nueva_poblacion=new Nodo_f[poblacion];
        for (i=0; i < poblacion; i++)
            sum=sum + evaluacion[i];
        for (i=0; i < poblacion; i++)
            evaluacion[i]=evaluacion[i] / sum;
        for (i=0; i < poblacion; i++)
        {
            if (i > 0)
                escoge[i]=evaluacion[i] + escoge[i - 1];
            else
                escoge[i]=evaluacion[i];
        }
        for (i=0; i < poblacion; i++)
        {
            aleatorio=Math.random();
            for (k=0; k < poblacion; k++)
                if (aleatorio <= escoge[k])
                {
                    Nodo_f nodin=escoge_nodo(arboles[k], 0);
                    nueva_poblacion[i]=nodin;
                    break;
                }
        }
        arboles    =nueva_poblacion;
        evaluacion2=evalua_general();
        for (i=0; i < poblacion; i++)
            if (evaluacion2[i] < peor)
            {
                peor  =evaluacion2[i];
                peor_i=i;
            }
        if (cont >= 1)
            for (i=0; i < poblacion; i++)
                if (evaluacion2[i] <= peor + .05)
                    arboles[i]=escoge_nodo(mejor, 0);

        for (i=0; i < poblacion; i++)
            if (evaluacion2[i] > mejores)
            {
                mejores=evaluacion2[i];
                nodo_i =i;
                mejor  =escoge_nodo(arboles[nodo_i], 0);
            }
        return arboles;
    }

    public int numero_nodos(Nodo_f nodo)
    {
        if (nodo == null)
            return 0;
        else
            return 1 + numero_nodos(nodo.izquierdo) + numero_nodos(nodo.derecho);
    }

    public void cambia_nodos(Nodo_f raiz, int num_nodo)
    {
        Random                 rnd = new Random();
        double                 aleatorio;
        Map<Integer, InfoNodo> mapa = new HashMap<Integer, InfoNodo>();
        indiceGlobal = 0;
        ordena(raiz, null, Tipos.RAIZ, mapa);
        InfoNodo infoDelNodo = mapa.get(num_nodo);
        aleatorio=rnd.nextInt(profundidad - 1);
        if (infoDelNodo.tipoDeHijo == Tipos.IZQUIERDO)
            infoDelNodo.papa.izquierdo =  forma_arbol(0, (int)aleatorio);
        else if (infoDelNodo.tipoDeHijo == Tipos.DERECHO)
            infoDelNodo.papa.derecho = forma_arbol(0, (int)aleatorio);
    }

    public Nodo_f escoge_nodo(Nodo_f raiz, int num_nodo)
    {
        List<Nodo_f> listaDeNodos = new ArrayList<Nodo_f>();
        ordena(raiz, listaDeNodos);
        Nodo_f papa = listaDeNodos.get(num_nodo);
        Nodo_f nuevo=new Nodo_f(papa.valor);
        recorre_copia(papa, nuevo);
        return nuevo;
    }

    public void recorre_copia(Nodo_f papa, Nodo_f raiz)
    {
        if (papa.izquierdo != null)
        {
            Nodo_f nodin=new Nodo_f(papa.izquierdo.valor);
            raiz.izquierdo=nodin;
            recorre_copia(papa.izquierdo, raiz.izquierdo);
        }
        if (papa.derecho != null)
        {
            Nodo_f nodin=new Nodo_f(papa.derecho.valor);
            raiz.derecho=nodin;
            recorre_copia(papa.derecho, raiz.derecho);
        }
    }

    public void intercambia_nodos(Nodo_f raiz, int num_nodo, Nodo_f aux1) throws RuntimeException
    {
        Map<Integer, InfoNodo> mapa = new HashMap<Integer, InfoNodo>();
        indiceGlobal = 0;
        ordena(raiz, null, Tipos.RAIZ, mapa);
        //System.out.println("ind:"+indiceGlobal+"num:"+num_nodo);
        InfoNodo infoDelNodo = mapa.get(num_nodo);
        try{
            if (infoDelNodo.tipoDeHijo == Tipos.IZQUIERDO)
                infoDelNodo.papa.izquierdo = aux1;
            else if (infoDelNodo.tipoDeHijo == Tipos.DERECHO)
                infoDelNodo.papa.derecho = aux1;
        }catch (Exception e)
        {
            for (Map.Entry<Integer, InfoNodo> pareja : mapa.entrySet())
                System.out.println(pareja.getKey());
            throw new RuntimeException();
        }
    }

    private void ordena(Nodo_f actual, Nodo_f papa, Tipos tipo, Map<Integer, InfoNodo> mapa)
    {
        if (actual != null)
        {
            InfoNodo info = new InfoNodo(papa, tipo);
            mapa.put(indiceGlobal, info);
            indiceGlobal = indiceGlobal + 1;
            ordena(actual.izquierdo, actual, Tipos.IZQUIERDO, mapa);
            ordena(actual.derecho, actual, Tipos.DERECHO, mapa);
        }
    }

    private void ordena(Nodo_f actual, List<Nodo_f> lista)
    {
        if (actual != null)
        {
            lista.add(actual);
            ordena(actual.izquierdo, lista);
            ordena(actual.derecho, lista);
        }
    }

    void imprime(Nodo_f nodo)
    {
        if (nodo != null)
        {
            System.out.print("(");
            System.out.print(nodo.valor);
            imprime(nodo.izquierdo);
            imprime(nodo.derecho);
            System.out.print(")");
        }
    }

//@Test
/*	public void pruebilla() {
        Nodo uno = new Nodo(1);
        Nodo dos = new Nodo(2);
        Nodo tres = new Nodo(3);
        Nodo cuatro = new Nodo(4);
        Nodo cinco = new Nodo(5);
        Nodo seis = new Nodo(6);
        Nodo siete = new Nodo(7);

        uno.izquierdo = dos;
        uno.derecho = seis;
        dos.izquierdo = tres;
        dos.derecho = cuatro;
        tres.derecho= cinco;
        tres.izquierdo=seis;
        cinco.izquierdo = seis;
        cinco.derecho = siete;

        imprime(uno);
        System.out.println("");
        imprime(cinco);
        System.out.println("");
        Nodo aux1=escoge_nodo(uno,1);
        System.out.println("aux 1");
        imprime(aux1);
        //intercambia_nodos(uno ,2, cinco);
        System.out.println("A");
        imprime(uno);
        System.out.println("");
        imprime(cinco);
        System.out.println("");
    }*/

    public void muta(double pm)
    {
        Random  rnd = new Random();
        double  aleatorio, aleatorio2;
        int     num_nodos=0;
        int     contador =0;
        boolean resultados;
        for (int i=0; i < (int)(poblacion * pm); i++)
        {
            aleatorio =rnd.nextInt(poblacion);
            num_nodos =numero_nodos(arboles[(int)aleatorio]);
            aleatorio2=rnd.nextInt(num_nodos);
            if ((int)aleatorio2 == 0)
                arboles[(int)aleatorio]=forma_arbol(0, (int)(rnd.nextInt(profundidad)));
            else
                cambia_nodos(arboles[(int)aleatorio], (int)aleatorio2);
        }
        evalua_general();
    }

    public void cruza(double pc)
    {
        double aleatorio1, aleatorio2, ale;
        Random rnd = new Random();
        //System.out.println("");
        for (int i=0; i < poblacion - 1; i=i + 2)
            if (Math.random() < pc)
            {
                nodos     =numero_nodos(arboles[i]);
                aleatorio1=rnd.nextInt(nodos);
                nodos     =numero_nodos(arboles[i + 1]);
                aleatorio2=rnd.nextInt(nodos);
                if (aleatorio1 < aleatorio2)
                    ale=aleatorio1;
                else
                    ale=aleatorio2;
                Nodo_f aux1=escoge_nodo(arboles[i], (int)ale);
                Nodo_f aux2=escoge_nodo(arboles[i + 1], (int)ale);
                if (ale == 0)
                    arboles[i]=escoge_nodo(aux2, 0);
                else
                    intercambia_nodos(arboles[i], (int)ale, aux2);
                if (ale == 0)
                    arboles[i + 1]=escoge_nodo(aux1, 0);
                else
                    intercambia_nodos(arboles[i + 1], (int)ale, aux1);
            }
        evalua_general();
    }

    public boolean converge(double [] evaluacion)
    {
        int    conb =0;
        double ind  =0;
        int    mejor=100;
        individuo=0;
        ind      =evaluacion[0];
        for (int i=1; i < poblacion; i++)
            if (evaluacion[i] > .995)
            {
                conb++;
                individuo=i;
            }
        System.out.print("con :" + conb);
        if (conb >= (int)(poblacion - 1) * .9)
            return true;
        else
            return false;
    }

    public boolean opera_boolean(int num, boolean a, boolean b)
    {
        switch (funciones[num])
        {
            case '&':
                return a & b;
            case '|':
                return a | b;
            case '~':
                if (a == true)
                    return false;
                else
                    return true;
            case '^':
                return a ^ b;
        }
        return false;
    }

    public double opera(int num, double a, double b)
    {
        switch (funciones[num])
        {
            case '+':
                return a + b;
            case '*':
                return a * b;
            case '-':
                return a - b;
            case '/':
                if (b == 0)
                    return 10000;
                else
                    return a / b;
            case 'c':
                return Math.cos(a);
            case 's':
                return Math.sin(a);
            case 'r':
                return Math.sqrt(a);
        }
        return 0;
    }

    private static class InfoNodo
    {
        Tipos tipoDeHijo;
        Nodo_f papa;

        public InfoNodo(Nodo_f papa, Tipos tipo)
        {
            this.papa       = papa;
            this.tipoDeHijo = tipo;
        }
    }
    public void ajuste(int indi)
    {
        double resultados=0;
        String puntos    ="";
        try
        {
            System.out.println("holo _graficare+" + re_funcion.length);
            //Crear un objeto File se encarga de crear o abrir acceso a un archivo que se especifica en su constructor
            File archivo=new File("Grafica_ajustes.txt");
            //Crear objeto FileWriter que sera el que nos ayude a escribir sobre archivo
            FileWriter escribir=new FileWriter(archivo, true);
            for (int j=0; j < re_funcion.length; j++)
            {
                resultados=evalua1(arboles[indi], j);
                puntos    =comparar[j] + " " + resultados + " " + re_funcion[j];
                escribir.write(puntos + "\n");
            }
            escribir.close();
        }catch (Exception e)
        {
            System.out.println("Error al escribir" + e);
        }
    }

    public void programa()
    {
        double [] evaluacion=new double[poblacion];
        double    media=0, mayor=0;
        int       i=0, in=0;
        int       num_nodos=0;
        String    puntos   ="";
        arma_poblacion();
        try
        {
            //Crear un objeto File se encarga de crear o abrir acceso a un archivo que se especifica en su constructor
            File archivo=new File("Grafica.txt");
            //Crear objeto FileWriter que sera el que nos ayude a escribir sobre archivo
            FileWriter escribir =new FileWriter(archivo, true);
            File       archivo_n=new File("Nodos.txt");
            //Crear objeto FileWriter que sera el que nos ayude a escribir sobre archivo
            FileWriter escribir2=new FileWriter(archivo_n, true);
            do {
                evaluacion=evalua_general();
                selecciona(evaluacion);
                muta(.15);
                cruza(.6);
                evaluacion=evalua_general();
                media     =0;
                mayor     =0;
                for (i=0; i < poblacion; i++)
                {
                    if (evaluacion[i] > mayor)
                    {
                        mayor=evaluacion[i];
                        in   =i;
                    }
                    media    =media + evaluacion[i];
                    num_nodos=num_nodos + numero_nodos(arboles[i]);
                }
                cont++;
                media    =media / poblacion;
                num_nodos=num_nodos / poblacion;
                puntos   =cont + " " + media + " " + mayor;
                escribir.write(puntos + "\n");
                puntos=cont + " " + num_nodos;
                escribir2.write(puntos + "\n");
                for (i=0; i < poblacion; i++)
                {
                    System.out.print("-------");
                    //recorre2(arboles[i]);
                    System.out.print(" evaluacion:" + evaluacion[i] + "nodos:" + numero_nodos(arboles[i]));
                }//file.deleted
            } while (converge(evaluacion) == false);
            escribir.close();
            escribir2.close();
            System.out.println("Generaciones:" + cont);
            /*for(i=0;i<poblacion;i++)
                {
                    System.out.print("-------");
                    //recorre2(arboles[i]);
                    System.out.print(" evaluacion:"+evaluacion[i]+"nodos:"+numero_nodos(arboles[i]));
                }//file.deleted
                System.out.println("Generaciones:"+cont);
               System.out.println("----");*/
        }catch (Exception e)
        {
            System.out.println("Error al escribir" + e);
        }
        recorre2(arboles[in]);
        ajuste(in);
    }
}
