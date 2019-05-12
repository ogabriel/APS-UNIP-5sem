package chatform;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.JTextField;
import javax.swing.JScrollPane;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.ScrollPaneConstants;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.ActionEvent;

public class Client extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5391582161763137020L;
	private JPanel contentPane;
	private JTextField inputText;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					// TODO: get the client usename
					Client frame = new Client();
					frame.setVisible(true);
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
		setAlwaysOnTop(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 464, 446);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		JScrollPane messages = new JScrollPane();
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
				sendMessage();
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
					sendMessage();
				}
			}
		});
		
		
		buttons.add(btnEnviarArquivo, BorderLayout.CENTER);
		
		JButton btnExit = new JButton("Desconectar");
		buttons.add(btnExit, BorderLayout.SOUTH);
	}
	
	private void sendMessage() {
		// TODO: send the result to the method that sends the text
		// LIKE: Send(getText());
		getText();
		inputText.setText("");
	}
	
	private void sendFile() {
		// TODO: send the result to the method that sends the file
		// LIKE: Send(getFile());
		getFile();
	}
	
	private String getText() {
		return inputText.getText();
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
		    inputText.setText(file); 
		}
		
		return file;
	}
}
