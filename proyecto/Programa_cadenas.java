public class Programa_cadenas
{
    public static void main(String[] args)
    {
        MinimizaCadennas modelo1=new MinimizaCadennas();
        double           pm=0, pc=0, evaluacion=0;
        int              individuos=300;
        String           cadena1   ="TATATGCGGCCCCTATAGTACTATTGACACACCCAGTCAACACCCACGATCACAGATGGACATATAA";
        String           cadena2   ="AATGCCGGTCCGGGGCGCTAAATATTACACAGTACAGTAACCCGCTGCCAATCCATGACGATCAGT";
        double           max       =(Math.pow(cadena1.length(), 2) + cadena1.length()) / 2;
        System.out.println("Max:" + max);
        int longitud=modelo1.encuentra_longitud(1, 2, max), entero=1, n_f=2;
        int generaciones=0, multiplo_longitud=0;
        pm=1 / longitud;
        pc=0.6;
        AGe_multifuncional<MinimizaCadennas> ag01=new AGe_multifuncional<MinimizaCadennas>(modelo1, longitud, individuos, pc, pm, n_f, cadena1, cadena2);
        //ag01.dominancia_fonseca(val);
        generaciones=ag01.aplica_ag();
    }
}
