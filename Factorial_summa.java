/**
 * Created by Ps1X on 30.05.2017.
 */

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

/**
 Find the sum of the digits in the number 100! (i.e. 100 factorial)
 {Correct answer: 648}

 */
public class Factorial_summa {
    static Map<Integer, BigInteger> cache = new HashMap<>();

    public static void main(String[] args) {
        BigInteger b = factorial(100); // Initialize a variable
        String s = b.toString(); // Transform the number into a string
        String[] array = s.split(""); // Array partitioning
        int result = 0; // Result obtained
        for (String s1 : array) { // Array values
            result += Integer.parseInt(s1); // Pars a char in a int and add to an existing one
        }
        System.out.println(result); // print result
    }
    private static BigInteger factorial (int i) {
        BigInteger big; // make int
        if (i == 0) { // Out of recursion
            return BigInteger.ONE;
        }
        if ((big = cache.get(i)) != null ) { // Check for an existing value
            return big;
        }
        big = BigInteger.valueOf(i).multiply(factorial(i - 1)); // recursion
        cache.put(i, big); // add to map
        return big;
    }
}