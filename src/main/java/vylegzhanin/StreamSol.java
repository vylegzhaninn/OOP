package vylegzhanin;

import java.util.Arrays;

public class StreamSol {
    public static boolean streamSolution(int[] arr){
        return Arrays.stream(arr).anyMatch(Prime::isPrime);
    }
}
