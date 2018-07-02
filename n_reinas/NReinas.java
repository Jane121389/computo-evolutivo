public class NReinas implements AG.Modelo
{
    int n = 4;
    public NReinas(int n)
    {
        this.n = n;
    }

    int maximo()
    {
        int max=0;
        max=(4 * n * n) - (6 * n) + 2;
        return max;
    }

    int [][] crea_matriz(int [] poblacion)
    {
        int      i=0, j=0, s=0;
        int [][] tablero=new int[n][n];
        for (i=0; i < n; i++)
            for (j=0; j < n; j++)
            {
                tablero[i][j]=poblacion[s];
                s++;
            }
        return tablero;
    }

    int valida_fila_columna(int [][] tablero)
    {
        int suma_f=0, suma_c=0, i=0, j=0, suma=0;
        for (i=0; i < n; i++)
        {
            suma_f=0;
            suma_c=0;
            for (j=0; j < n; j++)
            {
                if (tablero[i][j] == 1)
                    suma_f++;
                if (tablero[j][i] == 1)
                    suma_c++;
            }
            if (suma_f >= 0 && suma_f < n)
                suma=suma + (maximo() * (n - suma_f) / n);
            else if (suma_f > n)
                suma=suma + maximo();
            if (suma_c >= 0 && suma_c < n)
                suma=suma + (maximo() * (n - suma_f) / n);
            else if (suma_c > n)
                suma=suma + maximo();
            suma=suma + suma_f + suma_c;
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
            if (suma_d >= 0 && suma_d < n)
                suma_d=suma_d + (maximo() * (n - suma_d) / n);
            else if (suma_d > n)
                suma_d=suma_d + maximo();
            if (suma >= 0 && suma < n)
                suma=suma_d + (maximo() * (n - suma_d) / n);
            else if (suma > n)
                suma=suma + maximo();
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
            if (suma_d >= 0 && suma_d < n)
                suma_d=suma_d + (maximo() * (n - suma_d) / n);
            else if (suma_d > n)
                suma_d=suma_d + maximo();
            if (suma >= 0 && suma < n)
                suma=suma_d + (maximo() * (n - suma_d) / n);
            else if (suma > n)
                suma=suma + maximo();
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
        if (suma_d >= 0 && suma_d < n)
            suma_d=suma_d + (maximo() * (n - suma_d) / n);
        else if (suma_d > n)
            suma_d=suma_d + maximo();
        if (suma >= 0 && suma < n)
            suma=suma + suma_d + (maximo() * (n - suma_d) / n);
        else if (suma > n)
            suma=suma + maximo();
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
            tablero=crea_matriz(poblacion[i]);
            suma   =valida_fila_columna(tablero) + valida_diagonal(tablero);

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
}
