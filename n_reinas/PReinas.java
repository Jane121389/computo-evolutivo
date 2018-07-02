public class PReinas implements AGe.Modelo
{
    int n;
    public PReinas(int n)
    {
        this.n = n;
    }

    int maximo()
    {
        int max=0;
        max=(4 * n * n) - (6 * n) + 2;
        return max;
    }

    int encuentra_longitud()
    {
        int longitud=0;
        longitud=(int)(n * (Math.log(n) / Math.log(2)));
        return longitud;
    }

    int [][] crea_matriz(int [] poblacion)
    {
        int      i=0, j=0, s=0, columna=0, fila=0;
        int      div    =(int)(Math.log(n) / Math.log(2));
        int []   binario=new int[div];
        int [][] tablero=new int[n][n];
        for (i=0; i < n; i++)
            for (j=0; j < n; j++)
                tablero[i][j]=0;
        for (i=0; i < poblacion.length; i++)
        {
            binario[s]=poblacion[i];
            if (s < div - 1)
                s++;
            else
            {
                columna               =convierteANumero(binario);
                tablero[fila][columna]=1;
                fila++;
                s=0;
            }
        }
        return tablero;
    }

    int valida_fila(int [][] tablero)
    {
        int suma_f=0, i=0, j=0, suma=0;
        for (i=0; i < n; i++)
        {
            suma_f=0;
            for (j=0; j < n; j++)
                if (tablero[j][i] == 1)
                    suma_f++;
            if (suma_f > 1)
                suma=suma + (maximo() * suma_f);
            suma=suma + suma_f;
        }
        return suma;
    }

    int valida_diagonal(int [][] tablero)
    {
        int suma=0, i, j=0, s, suma_d=0, k=0, total=0;
        for (i=1; i < n; i++)
        {
            j     =0;
            suma_d=0;
            suma  =0;
            for (s=i; s < n; s++)
            {
                if (tablero[s][j] == 1)
                    suma_d++;
                if (tablero[j][s] == 1)
                    suma++;
                j++;
            }
            if (suma_d > 1)
                suma_d=suma_d + (maximo() * suma_d);
            if (suma > 1)
                suma=suma + (maximo() * suma);
            total=total + suma_d + suma;
        }
        for (i=n - 2; i >= 0; i--)
        {
            j     =0;
            suma_d=0;
            suma  =0;
            for (s=i; s >= 0; s--)
            {
                if (tablero[j][s] == 1)
                    suma++;
                if (tablero[s + 1][j + 1] == 1)
                    suma_d++;
                j++;
            }
            if (suma_d > 1)
                suma_d=suma_d + (maximo() * suma_d);
            if (suma > 1)
                suma=suma + (maximo() * suma);
            total=total + suma_d + suma;
        }
        k     =n - 1;
        suma_d=0;
        suma  =0;
        for (i=0; i < n; i++)
        {
            if (tablero[i][i] == 1)
                suma_d++;
            if (tablero[k][i] == 1)
                suma++;
            k--;
        }
        if (suma_d > 1)
            suma_d=suma_d + (maximo() * suma_d);
        if (suma > 1)
            suma=suma + suma + (maximo() * suma);
        total=total + suma_d + suma;
        return total;
    }

    @Override
    public double[] aptitud(int[][] poblacion)
    {
        double[] aptitudes = new double[poblacion.length];
        int      suma      =0;
        int [][] tablero   =new int[n][n];
        for (int i=0; i < poblacion.length; i++) {
            tablero     =crea_matriz(poblacion[i]);
            suma        =valida_fila(tablero) + valida_diagonal(tablero);
            aptitudes[i]=1.0 / suma;
        }
        return aptitudes;
    }

    @Override
    public void finales(int[][] poblacion, int ind)
    {
        int[][] tablero = crea_matriz(poblacion[ind]);
        for (int i=0; i < n; i++) {
            for (int j=0; j < n; j++)
                System.out.print(tablero[i][j]);
            System.out.println("");
        }
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
}
