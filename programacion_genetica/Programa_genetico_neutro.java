import java.io.*;
import java.util.*;
import java.io.File;
import java.io.FileWriter;
public class Programa_genetico_neutro
{
    int profundidad=0;
    int n_funciones;
    int [] elem_funciones;
    int n_elementos;
    char [] funciones;
    int [] elementos;
    int poblacion;
    boolean [][] comparar;
    Nodo arboles[];
    int nodos=0;
    boolean continuar;
    boolean [] re_funcion;
    Nodo aux         =null;
    int indiceGlobal = 0;
    int individuo    =0;
    int ver_elem[];
    int cont=0;
    Nodo mejor;
    double mejores=0;
    static enum Tipos
    {
        IZQUIERDO, DERECHO, RAIZ
    };
    int num_1=0, num_0=0, num_4=0;
    public void inicia(int n_funciones, int [] elem_funciones, int n_elementos, int profundidad, char [] funciones, int [] elementos, int poblacion, boolean [][] comparar, boolean [] re_funcion)
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

    public Nodo forma_arbol(int e, int profundidad)
    {
        Random rnd = new Random();
        double aleatorio;
        if (e == 0)
            aleatorio=rnd.nextInt(n_funciones);
        else
            aleatorio=rnd.nextInt(n_funciones + n_elementos);
        if (aleatorio < n_funciones && e < profundidad)
        {
            int  num  =elem_funciones[(int)aleatorio];
            Nodo nodin=new Nodo((int)aleatorio);
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
            Nodo nodo=new Nodo((int)aleatorio + n_funciones);
            return nodo;
        }
    }

    public void recorre(Nodo nodo, int fila)
    {
        int num=0;
        if (nodo != null)
        {
            System.out.print("(");
            num=nodo.valor;
            if (num >= n_funciones)
            {
                num=num - n_funciones;
                System.out.print(comparar[fila][elementos[num]]);
            }
            else
                System.out.print(funciones[num]);
            recorre(nodo.izquierdo, fila);
            recorre(nodo.derecho, fila);
            System.out.print(")");
        }
    }

    public void recorre2(Nodo nodo)
    {
        int num=0;
        if (nodo != null)
        {
            System.out.print("(");
            num=nodo.valor;
            if (num >= n_funciones)
            {
                num=num - n_funciones;
                System.out.print(elementos[num]);
            }
            else
                System.out.print(funciones[num]);
            recorre2(nodo.izquierdo);
            recorre2(nodo.derecho);
            System.out.print(")");
        }
    }

    public double evalua1(Nodo nodo, int fila)
    {
        if (nodo != null)
        {
            if (nodo.valor < n_funciones)
                return opera(nodo.valor, evalua1(nodo.izquierdo, fila), evalua1(nodo.derecho, fila));
            else
                return elementos[nodo.valor - n_funciones];
        }
        return 0;
    }

    public void numero_numeros(Nodo nodo)
    {
        int num=0;
        if (nodo != null)
        {
            num=nodo.valor;
            if (num >= n_funciones)
            {
                num=num - n_funciones;
                if (elementos[num] == 0)
                    num_0++;
                else if (elementos[num] == 1)
                    num_1++;
                else if (elementos[num] == 4)
                    num_4++;
            }
            numero_numeros(nodo.izquierdo);
            numero_numeros(nodo.derecho);
        }
    }

    public double [] evalua_general()
    {
        double    resultados;
        double [] resultados_total =new double[poblacion];
        double    contador         =0;
        int       num_nodos        =0;
        double    sum              =0;
        boolean   total            =true;
        int       resultado_general=10;
        for (int i=0; i < poblacion; i++)
        {
            resultados=Math.abs(resultado_general - evalua1(arboles[i], 0));
            if (resultados == 0)
                resultados_total[i]=2;
            else
                resultados_total[i]=1 / resultados;
        }
        return resultados_total;
    }

    public Nodo [] arma_poblacion()
    {
        int i=0;
        arboles=new Nodo[poblacion];
        for (i=0; i < poblacion; i++)
            arboles[i]=forma_arbol(0, profundidad);
        return arboles;
    }

    public Nodo[] selecciona(double [] evaluacion)
    {
        double    aleatorio, sum=0, peor=1000;
        int       i=0, k=0, peor_i=0, nodo_i=0;
        double [] evaluacion2    =new double[poblacion];
        double [] escoge         =new double[poblacion];
        Nodo []   nueva_poblacion=new Nodo[poblacion];
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
                    Nodo nodin=escoge_nodo(arboles[k], 0);
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
            arboles[peor_i]=escoge_nodo(mejor, 0);
        for (i=0; i < poblacion; i++)
            if (evaluacion2[i] > mejores)
            {
                mejores=evaluacion2[i];
                nodo_i =i;
            }
        mejor  =escoge_nodo(arboles[nodo_i], 0);
        mejores=0;
        return arboles;
    }

    public int numero_nodos(Nodo nodo)
    {
        if (nodo == null)
            return 0;
        else
            return 1 + numero_nodos(nodo.izquierdo) + numero_nodos(nodo.derecho);
    }

    public void cambia_nodos(Nodo raiz, int num_nodo)
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

    public Nodo escoge_nodo(Nodo raiz, int num_nodo)
    {
        List<Nodo> listaDeNodos = new ArrayList<Nodo>();
        ordena(raiz, listaDeNodos);
        Nodo papa = listaDeNodos.get(num_nodo);
        Nodo nuevo=new Nodo(papa.valor);
        recorre_copia(papa, nuevo);
        return nuevo;
    }

    public void recorre_copia(Nodo papa, Nodo raiz)
    {
        if (papa.izquierdo != null)
        {
            Nodo nodin=new Nodo(papa.izquierdo.valor);
            raiz.izquierdo=nodin;
            recorre_copia(papa.izquierdo, raiz.izquierdo);
        }
        if (papa.derecho != null)
        {
            Nodo nodin=new Nodo(papa.derecho.valor);
            raiz.derecho=nodin;
            recorre_copia(papa.derecho, raiz.derecho);
        }
    }

    public void intercambia_nodos(Nodo raiz, int num_nodo, Nodo aux1) throws RuntimeException
    {
        Map<Integer, InfoNodo> mapa = new HashMap<Integer, InfoNodo>();
        indiceGlobal = 0;
        ordena(raiz, null, Tipos.RAIZ, mapa);
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

    private void ordena(Nodo actual, Nodo papa, Tipos tipo, Map<Integer, InfoNodo> mapa)
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

    private void ordena(Nodo actual, List<Nodo> lista)
    {
        if (actual != null)
        {
            lista.add(actual);
            ordena(actual.izquierdo, lista);
            ordena(actual.derecho, lista);
        }
    }

    void imprime(Nodo nodo)
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
    public void pruebilla()
    {
        Nodo uno    = new Nodo(1);
        Nodo dos    = new Nodo(2);
        Nodo tres   = new Nodo(3);
        Nodo cuatro = new Nodo(4);
        Nodo cinco  = new Nodo(5);
        Nodo seis   = new Nodo(6);
        Nodo siete  = new Nodo(7);

        uno.izquierdo   = dos;
        uno.derecho     = seis;
        dos.izquierdo   = tres;
        dos.derecho     = cuatro;
        tres.derecho    = cinco;
        tres.izquierdo  =seis;
        cinco.izquierdo = seis;
        cinco.derecho   = siete;

        imprime(uno);
        System.out.println("");
        imprime(cinco);
        System.out.println("");
        Nodo aux1=escoge_nodo(uno, 1);
        System.out.println("aux 1");
        imprime(aux1);
        //intercambia_nodos(uno ,2, cinco);
        System.out.println("A");
        imprime(uno);
        System.out.println("");
        imprime(cinco);
        System.out.println("");
    }

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
        double aleatorio1, aleatorio2;
        Random rnd = new Random();
        //System.out.println("");
        for (int i=0; i < poblacion - 1; i=i + 2)
            if (Math.random() < pc)
            {
                nodos     =numero_nodos(arboles[i]);
                aleatorio1=rnd.nextInt(nodos);
                Nodo aux1=escoge_nodo(arboles[i], (int)aleatorio1);
                nodos     =numero_nodos(arboles[i + 1]);
                aleatorio2=rnd.nextInt(nodos);
                Nodo aux2=escoge_nodo(arboles[i + 1], (int)aleatorio2);
                if ((int)aleatorio1 == 0)
                    arboles[i]=aux1;
                else
                    intercambia_nodos(arboles[i], (int)aleatorio1, aux2);
                if ((int)aleatorio2 == 0)
                    arboles[i + 1]=aux2;
                else
                    intercambia_nodos(arboles[i + 1], (int)aleatorio2, aux1);
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
            if (evaluacion[i] == 2)
            {
                conb++;
                individuo=i;
            }
        if (conb >= (int)(poblacion - 1))
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
        }
        return 0;
    }

    private static class InfoNodo
    {
        Tipos tipoDeHijo;
        Nodo papa;

        public InfoNodo(Nodo papa, Tipos tipo)
        {
            this.papa       = papa;
            this.tipoDeHijo = tipo;
        }
    }
    public void programa()
    {
        double [] evaluacion=new double[poblacion];
        double    media=0, mayor=0;
        int       i        =0;
        int       num_nodos=0;
        String    puntos   ="";
        arma_poblacion();
        try
        {
            //Crear un objeto File se encarga de crear o abrir acceso a un archivo que se especifica en su constructor
            File archivo=new File("Grafica_numeros.txt");
            //Crear objeto FileWriter que sera el que nos ayude a escribir sobre archivo
            FileWriter escribir=new FileWriter(archivo, true);
            //Crear un objeto File se encarga de crear o abrir acceso a un archivo que se especifica en su constructor
            File archivo2=new File("Grafica_nodos.txt");
            //Crear objeto FileWriter que sera el que nos ayude a escribir sobre archivo
            FileWriter escribir2=new FileWriter(archivo2, true);
            do {
                cont++;
                evaluacion=evalua_general();
                selecciona(evaluacion);
                muta(.15);
                cruza(.6);
                evaluacion=evalua_general();
                mayor     =0;
                num_nodos =0;
                for (i=0; i < poblacion; i++)
                {
                    if (evaluacion[i] > mayor)
                    {
                        mayor    =evaluacion[i];
                        individuo=i;
                    }
                    media    =media + evaluacion[i];
                    num_nodos=num_nodos + numero_nodos(arboles[i]);
                    numero_numeros(arboles[i]);
                    //System.out.print(" evaluacion:"+evaluacion[i]);
                }
                puntos=cont + " " + num_0 + " " + num_1 + " " + num_4;
                escribir.write(puntos + "\n");
                puntos=cont + " " + (num_nodos / poblacion);
                escribir2.write(puntos + "\n");
                num_4=0;
                num_1=0;
                num_0=0;
            } while (cont < 1500);
            escribir.close();
            escribir2.close();
            /*for(i=0;i<poblacion;i++)
                {
                    System.out.print("-------");
                    recorre2(arboles[i]);
                    System.out.print(" evaluacion:"+evaluacion[i]+"\n");
                }*/

            System.out.println("Generaciones:" + cont);
            System.out.println("----");
        }catch (Exception e)
        {
            System.out.println("Error al escribir");
        }
        recorre2(arboles[individuo]);
    }
}
