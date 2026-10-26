namespace Project;

public sealed class Fibonacci
{
    public static int Calcul(int n)
    {
        if (n < 2)
            return n;

        int counter =  1;
        int result = 0;
        int previous = 0;
        int current = 1;

        while (counter < n)
        {
            result = previous + current;
            Console.WriteLine($"{previous} {current} {result}");
            previous = current;
            current = result;
            ++ counter;
        }
        return result;
    }
}