import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class MyChat implements Runnable{
    ServerSocket serverSocket;

    ArrayList<Client> clients = new ArrayList<>();
    MyChat() throws IOException {
        serverSocket = new ServerSocket(1234);
    }

    @Override
    public void run(){
        while (true){
            System.out.println("Waiting...");
            try {
                Socket socket = serverSocket.accept();
                System.out.println("Client connected!");
                clients.add(new Client(socket, this));
            }catch (IOException e){
                e.printStackTrace();
            }
        }

    }
    void sendAll(String message){
        for(Client client : clients){
            client.receive(message);
        }
    }

    public static void main(String[] args) throws IOException{
        new MyChat().run();
    }
}
