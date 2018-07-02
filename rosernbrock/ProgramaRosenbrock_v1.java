public class ProgramaRosenbrock_v1
{
    public static void main(String[] args)
    {
        MinimizaRosenbrock modelo1 =new MinimizaRosenbrock();
        int                longitud =modelo1.encuentra_longitud(), entero=1;
        double             pm =0, pc=0, evaluacion=0;
        int                individuos =longitud;
        int                generaciones=0, multiplo_longitud=0;
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

        pm=3.676470588235294E-4;
        pc=0.7;
        AGe<MinimizaRosenbrock> ag01=new AGe<MinimizaRosenbrock>(modelo1, longitud, individuos, pc, pm);
        generaciones=ag01.aplica_ag();
    }
}
