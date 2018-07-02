import java.io.*;
import java.util.*;
public class Paridad
{
    public static void main(String args[])
    {
        Random                     rnd = new Random();
        Scanner                    sc  =new Scanner(System.in);
        double                     aleatorio;
        int []                     elem_funciones={
            2, 2, 1
        };
        char []                    funciones={
            '&', '|', '~'
        };
        int []                     elementos={
            0, 1, 2
        };
        boolean [][]               comparar  ={{true, true, true}, {true, true, false}, {true, false, true}, {true, false, false}, {true, false, false}, {false, true, false}, {false, false, true}, {false, false, false}};
        boolean []                 re_funcion={
            true, false, false, true, true, true, true, false
        };
        int                        n_funciones=funciones.length;
        int                        n_elementos=elementos.length;
        int                        profundidad=10;
        int                        poblacion  =500;
        Programa_genetico_correcto arbol      =new Programa_genetico_correcto();
        arbol.inicia(n_funciones, elem_funciones, n_elementos, profundidad, funciones, elementos, poblacion, comparar, re_funcion);
        arbol.programa();
    }
}
