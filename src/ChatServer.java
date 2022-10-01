import sun.misc.Cleaner;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
//забыла в ответе указать версию:
//openjdk-18
public class ChatServer {
    List<Client> clients=new ArrayList<>();
    ServerSocket serverSocket;
    int clientsNum=0;
    //добавим краски (увидеть можно в putty)
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
    ChatServer() throws IOException {
        // создаем серверный сокет на порту 1234
         this.serverSocket= new ServerSocket(1234);
    }
    void sendAll(String message, String clientName){
        for(Client client : clients)client.receive(ANSI_RED + clientName+": "+ANSI_RESET+message);
    }
    void deleteClient(Client client){
        clients.remove(client);
    }
    public void run(){
        while(true) {
            System.out.println("Waiting...");
            try {
                // ждем клиента из сети
                Socket socket = serverSocket.accept();
                System.out.println("Client connected!");
                // создаем клиента на своей стороне
                this.clientsNum++;
                clients.add(new Client(socket,"client"+clientsNum,this));
                System.out.println("current connections:"+clients.size());

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    public static void main(String[] args) throws IOException {
        new ChatServer().run();

    }
}

