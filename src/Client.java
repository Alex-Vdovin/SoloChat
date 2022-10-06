import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

public class Client implements Runnable{
    Socket socket;
    MyChat chat;
    Scanner in;
    PrintStream out;
    Client(Socket socket, MyChat chat){
        this.socket = socket;
        this.chat = chat;
        new Thread(this).start();
    }
    public void run(){
        try {
            InputStream is = socket.getInputStream();
            OutputStream os = socket.getOutputStream();

            in = new Scanner(is);
            out = new PrintStream(os);

            out.println("Hello everyone!");
            String input = in.nextLine();
            while (!input.equals("bye")){
                chat.sendAll(input);
                input = in.nextLine();
            }
            socket.close();

        }catch (IOException e){
            e.printStackTrace();
        }

    }
    void receive(String message){
        out.println(message);
    }
}
