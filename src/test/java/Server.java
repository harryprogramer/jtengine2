import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(2020);
        Socket socket = serverSocket.accept();
        BufferedReader inputStream  = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter printWriter = new PrintWriter(socket.getOutputStream(), true);
        while(true){
            int data = inputStream.read();
            System.out.println("data: " + data);
            printWriter.println("sieam siema otej prze kady");
            if(inputStream.readLine() == null){
                break;
            }
        }
    }
}
