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

    public Server(Socket connection) throws IOException {
        this.connection = connection;

        try {
            // Reader
            inS = connection.getInputStream();
            inSReader = new InputStreamReader(inS);
            bufferReader = new BufferedReader(inSReader);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {

            OutputStream ouS = this.connection.getOutputStream();
            Writer osWriter = new OutputStreamWriter(ouS);
            BufferedWriter bufferWriter = new BufferedWriter(osWriter);

            clients.add(bufferWriter);
            String current_user = this.bufferReader.readLine();
            users.add(current_user);
            String msg = "";
            System.out.println(current_user + " Connected");

            while (!("Disconnect " + current_user).equalsIgnoreCase(msg) && msg != null) {
                msg = this.bufferReader.readLine();
                broadCast(msg);
                System.out.println(current_user + " [listener] " + msg);
            }

            int index = users.indexOf(current_user);
            clients.remove(index);
            users.remove(index);
        } catch (Exception e) {
            e.printStackTrace();
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
}
