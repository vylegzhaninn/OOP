package worker;

import config.Config;
import java.io.PrintWriter;
import java.net.Socket;

public class Worker extends Config {
    public static void main(String[] args) {
        System.out.println("Работник работает");
        try (Socket socket = new Socket("localhost", PORT)){
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            while (true) {
                out.println("Салам");
                Thread.sleep(1000);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
