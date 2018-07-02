public class ProgramaKursawe
{
    public static void main(String[] args)
    {
        MinimizaKursawe modelo1 =new MinimizaKursawe();
        int             longitud =modelo1.encuentra_longitud(), entero=1, n_f=2;
        double          pm =0, pc=0, evaluacion=0;
        int             individuos =longitud;
        int             generaciones=0, multiplo_longitud=0;
        individuos=8;
        double val[][]={{1, 2}, {3, 4}, {2, 1}, {0, 0}, {4, 8}, {9, 10}, {11, 7}, {4, 2}};
        for (multiplo_longitud=16; multiplo_longitud < 20; multiplo_longitud=multiplo_longitud + 2)
            for (pc=0.7; pc < .9; pc=pc + .1)
            {
                entero=1;
                for (individuos=longitud; entero < 2; individuos=(longitud * entero))
                {
                    entero++;
                    for (int i=0; i < 5; i++)
                    {
                        pm=(double)(1.0 / (longitud * (multiplo_longitud)));
                        AGe<MinimizaRosenbrock> ag01=new AGe<MinimizaRosenbrock>(modelo1, longitud, individuos, pc, pm);
                        generaciones=ag01.aplica_ag();
                        evaluacion  =evaluacion + (generaciones * individuos);
                    }
                    evaluacion=evaluacion / 5;
                    System.out.println("El promedio de la evaluacion=" + evaluacion + ", con número de individuos=" + individuos + ", con probabilidad de mutacion=" + pm + " probabilidad de cruza=" + pc);
                }
            }

        pm=0.005;
        pc=0.6;

        System.out.println("adcsdf" + n_f + "sbd" + longitud);
        AGe_multifuncional<MinimizaKursawe> ag01=new AGe_multifuncional<MinimizaKursawe>(modelo1, longitud, individuos, pc, pm, n_f);

        //ag01.dominancia_fonseca(val);
        generaciones=ag01.aplica_ag();
    }
}
