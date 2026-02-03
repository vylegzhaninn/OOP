package vylegzhanin;

public class SequentialSol {
    public static boolean sequentialSolution(int[] arr){
        for (int el : arr){
            if (Prime.isPrime(el)) return true;
        }
        return false;
    }
}
