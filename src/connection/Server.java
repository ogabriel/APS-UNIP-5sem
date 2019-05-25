package connection;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class Server extends Thread {
    private static ArrayList<BufferedWriter> clients = new ArrayList<>();
    private static ArrayList<String> users = new ArrayList<>();
    private Socket connection;
    private InputStream inS;
    private InputStreamReader inSReader;
    private BufferedReader bufferReader;
    public String currentUser = "";

    public Server(Socket connection) throws IOException {
        this.connection = connection;

        try {
            // Reader
            bufferReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {

            BufferedWriter bufferWriter = new BufferedWriter(new OutputStreamWriter(this.connection.getOutputStream()));

            clients.add(bufferWriter);
            currentUser = this.bufferReader.readLine();
            users.add(currentUser);

            String msg = "Text&" + currentUser + " Conectado";
            String line = "";

            while (!("Text&Disconnect " + currentUser).equalsIgnoreCase(msg) && msg != null) {
                broadCast(msg);
                System.out.println(currentUser + " [Server(run)] " + msg);
                msg = this.bufferReader.readLine();
            }

            removeUser(currentUser);

            broadCast("Text&Usuário " + currentUser + " Desconectado");
        } catch (Exception e) {
            e.printStackTrace();
            removeUser(currentUser);
        }
    }

    public void broadCast(String msg) {
        for (BufferedWriter bufferClient : clients) {
            try {
                bufferClient.write(msg + "\r\n");
                System.out.println("[Broadcast] " + msg);
                bufferClient.flush();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void removeUser(String user) {
        int index = users.indexOf(user);
        clients.remove(index);
        users.remove(index);
    }
}
