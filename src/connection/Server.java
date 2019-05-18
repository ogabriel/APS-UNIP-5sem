package connection;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class Server extends Thread{
	private static ArrayList<BufferedWriter> clients;
	// private static ArrayList<String> users;
	private Socket con;
	private InputStream in;
	private InputStreamReader inr;
	private BufferedReader bfr;

	public Server (Socket con) throws IOException {
		this.con = con;
		this.clients = new ArrayList<>();
		// this.users = new ArrayList<>();

		// Writter
		OutputStream ou = this.con.getOutputStream();
		Writer ouw = new OutputStreamWriter(ou);
		BufferedWriter bfw = new BufferedWriter(ouw);

		clients.add(bfw);

		try {
			// Reader
            this.in = con.getInputStream();
            this.inr = new InputStreamReader(this.in);
            this.bfr = new BufferedReader(this.inr);
        } catch (IOException e) {
            e.printStackTrace();
        }
	}

	@Override
	public void run() {
		try {
			String msg;

            while(true) {
                msg = this.bfr.readLine();
				System.out.println(msg);
                this.broadCast(msg);
                System.out.println(msg);
            }
        } catch (Exception e) {
			e.printStackTrace();
        }
	}

	public void broadCast(String msg) throws IOException {
		for(BufferedWriter bw : clients) {
			bw.write(msg + "\r\n");
			bw.flush();
		}
	}
}
