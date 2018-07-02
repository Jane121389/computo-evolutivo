public class ProgramaBeales
{
    public static void main(String[] args)
    {
        MinimizaBeales modelo1 =new MinimizaBeales();
        int            longitud =modelo1.encuentra_longitud(), entero=1;
        double         pm =0, pc=0, evaluacion=0;
        int            individuos =longitud;
        int            generaciones=0, multiplo_longitud=0;
        for (multiplo_longitud=6; multiplo_longitud < 10; multiplo_longitud=multiplo_longitud + 2)
            for (pc=0.6; pc < .8; pc=pc + .1)
            {
                entero=1;
                for (individuos=longitud; entero < 2; individuos=(longitud * entero))
                {
                    entero++;
                    for (int i=0; i < 5; i++)
                    {
                        pm=(double)(1.0 / (longitud * (multiplo_longitud)));
                        AGe<MinimizaBeales> ag01=new AGe<MinimizaBeales>(modelo1, longitud, individuos, pc, pm);
                        generaciones=ag01.aplica_ag();
                        evaluacion  =evaluacion + (generaciones * individuos);
                    }
                    evaluacion=evaluacion / 5;
                    pro System.out.println("El promedio de la evaluacion=" + evaluacion + ", con número de individuos=" + individuos + ", con probabilidad de mutacion=" + pm + " probabilidad de cruza=" + pc);
                }
            }
        pm=0.004901960784313725;
        pc=0.6;
        AGe<MinimizaBeales> ag01=new AGe<MinimizaBeales>(modelo1, longitud, individuos, pc, pm);
        generaciones=ag01.aplica_ag();
    }
}
