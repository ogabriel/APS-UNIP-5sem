package chatform;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import javax.swing.JTextField;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.ScrollPaneConstants;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.Socket;
import java.awt.event.ActionEvent;

public class Client extends JFrame {

	/**
	 * Socket variables
	 */
	private Socket socket;
	private OutputStream ou ;
	private Writer ouw;
	private BufferedWriter bfw;	
	
	/**
	 * Form variables
	 */
	private static final long serialVersionUID = 5391582161763137020L;
	private JPanel contentPane;
	private JTextField inputText;
	private JScrollPane messages;
	private JTextArea output;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					// TODO: get the client username
					Client frame = new Client();
					frame.setVisible(true);
					frame.establishConnection();
					frame.listenConnection();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Client() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 600, 500);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		setLocationRelativeTo(null);
		setTitle("Aplicação de Conversa (Cliente)");
		
		output = new JTextArea();
		output.setEditable(false);
		
		messages = new JScrollPane(output);
		messages.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
		contentPane.add(messages, BorderLayout.CENTER);
		
		JPanel interactive = new JPanel();
		contentPane.add(interactive, BorderLayout.SOUTH);
		interactive.setLayout(new BorderLayout(0, 0));
		
		inputText = new JTextField();
		interactive.add(inputText, BorderLayout.CENTER);
		inputText.setColumns(10);
		
		JPanel buttons = new JPanel();
		interactive.add(buttons, BorderLayout.EAST);
		buttons.setLayout(new BorderLayout(0, 0));
		
		JButton btnSend = new JButton("Enviar");
		btnSend.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					sendMessage(getText());
				} catch (IOException e1) {
					writeOutput("CAGOU");
					e1.printStackTrace();
				}
			}
		});
		buttons.add(btnSend, BorderLayout.NORTH);
		
		JButton btnEnviarArquivo = new JButton("Enviar arquivo");
		btnEnviarArquivo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				sendFile();
			}
		});
		
		inputText.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_ENTER) {
					try {
						sendMessage(getText());
					} catch (IOException e1) {
						writeOutput("CAGOU");
						e1.printStackTrace();
					}
				}
			}
		});
		
		
		buttons.add(btnEnviarArquivo, BorderLayout.CENTER);
		
		JButton btnExit = new JButton("Desconectar");
		buttons.add(btnExit, BorderLayout.SOUTH);
	}
	
	private void sendMessage(String msg) throws IOException {
		// TODO: send the result to the method that sends the text
		// LIKE: Send(getText());
		if(msg.equals("Sair")) {
			bfw.write("Desconectado \r\n");
			writeOutput("Desconectado \r\n");
		}else {
			bfw.write(msg+"\r\n");
			writeOutput( "meu pau" + " diz -> " + getText()+"\r\n");
		}
		bfw.flush();
		
		writeOutput(getText());
		inputText.setText("");
	}
	
	private void sendFile() {
		// TODO: send the result to the method that sends the file
		// LIKE: Send(getFile());
		writeOutput("Você enviou o arquivo: " + getFile());
	}
	
	private String getText() {
		return inputText.getText();
	}
	
	private void writeOutput(String phrase) {
		output.append(phrase + "\r\n");
	}
	
	private String getFile() {
		JFileChooser chooseFile = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter("Apenas arquivos de texto e imagens", "jpg", "txt", "png", "xml");
		chooseFile.setCurrentDirectory(new java.io.File("."));
		chooseFile.setAcceptAllFileFilterUsed(false);
		chooseFile.setFileFilter(filter);
		String file = "";
		
		if(chooseFile.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
			file = chooseFile.getSelectedFile().getAbsolutePath(); 
		}
		return file;
	}
	
	private void establishConnection() throws IOException {
		socket = new Socket("127.0.0.1", 4444);
	    ou = socket.getOutputStream();
	    ouw = new OutputStreamWriter(ou);
	    bfw = new BufferedWriter(ouw);
	    bfw.write("minha pica" + "\r\n");
	    bfw.flush();
	}
	
	private void listenConnection() throws IOException {
		InputStream in = socket.getInputStream();
	    InputStreamReader inr = new InputStreamReader(in);
	    BufferedReader bfr = new BufferedReader(inr);
	    String msg = "";
	    
	    while(!"Sair".equalsIgnoreCase(msg)) {
	    	if(bfr.ready()) {
	    		msg = bfr.readLine();
	    		if(msg.equals("Sair"))
	    			output.append("Servidor caiu! \r\n");
	    		else
	    			output.append(msg+"\r\n");
	    	}
	    }
	}
}
