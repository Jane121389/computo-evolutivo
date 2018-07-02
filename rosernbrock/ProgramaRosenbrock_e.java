public class ProgramaRosenbrock_e
{
    public static void main(String[] args)
    {
        MinimizaRosenbrock modelo1 =new MinimizaRosenbrock();
        int                longitud =modelo1.encuentra_longitud(), entero=1;
        double             pm =0, pc=0, evaluacion=0;
        int                individuos =longitud;
        int                generaciones=0, multiplo_longitud=0;
        pm=3.26797385620915E-4;
        pc=0.6;
        AG_elitista<MinimizaRosenbrock> ag01=new AG_elitista<MinimizaRosenbrock>(modelo1, longitud, individuos, pc, pm);
        generaciones=ag01.aplica_ag();
    }
}
