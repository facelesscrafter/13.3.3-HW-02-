import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Client implements Runnable {
    Socket socket;
    String name;
    Scanner in;
    PrintStream out;
    ChatServer server;

    public Client(Socket socket,String name,ChatServer server) {
        this.socket = socket;
        this.name=name;
        this.server=server;
        // запускаем поток
        new Thread(this).start();
    }
    void receive(String message){
        out.println(message);
    }
    void disconnect(){
        System.out.println("Client disconnected. Waiting another client...");
        server.deleteClient(this);
    }
    public void run() {
        try {
            // получаем потоки ввода и вывода
            InputStream is = socket.getInputStream();
            OutputStream os = socket.getOutputStream();

            // создаем удобные средства ввода и вывода
            in = new Scanner(is);
            out = new PrintStream(os);

            // читаем из сети и пишем в сеть
            out.println("Welcome to our chat \"Mountains are best!!!\"");
            String input = in.nextLine();
            while (!input.equals("bye")) {
                server.sendAll(input,this.name);
                input = in.nextLine();
            }
            this.disconnect();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //при отключении клиента от сервера не через "bye" in.nextLine() не получает ожидаемую строку
        // и падает с NoSuchElementException, воспользуемся этим
        catch (NoSuchElementException e){
           // e.printStackTrace();
            this.disconnect();

        }
    }
}
