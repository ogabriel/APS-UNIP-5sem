package connection;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.Buffer;
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

            while (!"Disconnect".equalsIgnoreCase(msg) && msg != null) {
                msg = this.bufferReader.readLine();
                System.out.println(msg);
                // close the thread
                try {
                    bufferWriter.flush();
                } catch (IOException e) {
                    int index = users.indexOf(current_user);
                    clients.remove(index);
                    users.remove(index);
                    break;
                }
                this.broadCast(bufferWriter, msg);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void broadCast(BufferedWriter bInput, String msg) throws IOException {
        BufferedWriter bufferWriterOut;

        for (BufferedWriter bufferClient : clients) {
            bufferWriterOut = bufferClient;
            if (!(bufferWriterOut == bInput)) {
                bufferClient.write(msg + "\r\n");
                bufferClient.flush();
            }
        }
    }
}
