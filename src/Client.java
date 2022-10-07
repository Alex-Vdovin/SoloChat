import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

public class Client implements Runnable {
    Socket socket;
    MyChat chat;
    Scanner in;
    PrintStream out;
    String name;

    Client(Socket socket, MyChat chat) {
        this.socket = socket;
        this.chat = chat;
        new Thread(this).start();
    }

    public void run() {
        try {
            InputStream is = socket.getInputStream();
            OutputStream os = socket.getOutputStream();

            in = new Scanner(is);
            out = new PrintStream(os);

            out.println("Hello everyone!");
            out.println("Enter your name: ");

            name = in.nextLine().trim();

            // Проверяем уникальность имени
            while (nameCheck(name)){
                name = in.nextLine().trim();
            }

            chat.sendAll(name + " joined our chat!");
            String input = in.nextLine();
            while (!input.equals("bye")) {
                //Проверяем что сообщение не приватное и выводим текст
                if(!privateCall(input)){
                    chat.sendAll(name + ": " + input);
                }
                input = in.nextLine();
            }
            chat.sendAll(name + ": bye");
            chat.sendAll(name + " left our chat(((");
            socket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    void receive(String message) {
        out.println(message);
    }

    //Метод для написания приватного сообщения (только одному клиенту)
    boolean privateCall(String message){
        if(message.substring(0, this.name.length() +3).contains(this.name + ">>")){
            out.println("You wanna type to yourself? That's funny)");
            return true;
        }
        for (Client client : chat.clients) {
            if (message.substring(0, client.name.length() +3).contains(client.name + ">>")) {
                client.receive("Private from " + this.name + ": " + message.substring(client.name.length() + 2).trim());
                return true;
            }
        }
        return false;
    }

    //Метод проверки уникальности имени + проверка, что имя не пустое
    boolean nameCheck(String name){
        if(name.trim().isEmpty()){
            out.println("Type not empty name");
            return true;
        }
        for(String check : chat.names){
            if(name.equals(check)){
                out.println("This name is reserved");
                return true;
            }
        }
        chat.names.add(name.trim());
        return false;
    }
}
