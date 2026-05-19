package worker;

import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Arrays;

public class Worker {
    protected static final int PORT = 6767;

    public static void main(String[] args) {
        System.out.println("Работник работает");

        try (
            Socket socket = new Socket("localhost", PORT);
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        ){
            int[] chunk = (int[]) in.readObject();
            System.out.println("получен масив" + Arrays.toString(chunk));
            for (int el : chunk){
                if (!isPrime(el)){
                    out.println("beep");
                    System.out.println("Составное число найдено");
                    break;
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    static boolean isPrime(int n) {
        if (n < 2) return false;
        for (int i = 2; i * i <= n; i++) {
            if (n % i == 0) return false;
        }
        return true;
    }
}
