public class ProgramaFonseca
{
    public static void main(String[] args)
    {
        MinimizaFonseca modelo1 =new MinimizaFonseca();
        int             longitud =modelo1.encuentra_longitud(), entero=1, n_f=2;
        double          pm =0, pc=0, evaluacion=0;
        int             individuos =longitud;
        int             generaciones=0, multiplo_longitud=0;
        pm=0.005;
        pc=0.6;

        System.out.println("adcsdf" + n_f + "sbd" + longitud);
        AGe_multifuncional<MinimizaFonseca> ag01=new AGe_multifuncional<MinimizaFonseca>(modelo1, longitud, individuos, pc, pm, n_f);

        //ag01.dominancia_fonseca(val);
        generaciones=ag01.aplica_ag();
    }
}
