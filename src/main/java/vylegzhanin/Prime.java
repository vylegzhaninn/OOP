package vylegzhanin;

public class Prime {
    public static boolean isPrime(int el){
        if (el < 2) return false;
        for (int i = 2; i * i <= el; i++){
            if (el % i == 0) return false;
        }
        return true;
    }
}
