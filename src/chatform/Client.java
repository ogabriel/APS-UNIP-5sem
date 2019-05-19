package chatform;

import java.awt.BorderLayout;
import java.awt.event.*;

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
import javax.swing.text.DefaultCaret;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

import java.net.Socket;

public class Client extends JFrame implements Runnable {

    /**
     * Socket variables
     */
    private Socket socket;
    private OutputStream outS;
    private Writer outSWriter;
    private BufferedWriter bufferWriter;

    /**
     * Client variables
     */
    private String user;
    private String serverIP;
    private int serverPort;

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
    public void run() {
        try {
            this.setTitle(this.user);
            this.establishConnection();
            this.listenConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Create the frame.
     */
    public Client(String[] args) {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.addWindowListener(new WindowAdapter() {
                                   @Override
                                   public void windowClosing(WindowEvent e) {
                                       if(!socket.isClosed()) {
                                           try {
                                               disconnect();
                                           } catch (Exception ex) {
                                               ex.printStackTrace();
                                           }
                                       }

                                       Start start = new Start();
                                       start.setVisible(true);
                                       dispose();
                                   }
                               }
        );

        setBounds(100, 100, 600, 500);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(new BorderLayout(0, 0));
        setLocationRelativeTo(null);
        setTitle("Aplicação de Conversa (Cliente)");

        output = new JTextArea();
        output.setEditable(false);
        output.setLineWrap(true);

        messages = new JScrollPane(output);
        messages.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        DefaultCaret caretOutput = (DefaultCaret) output.getCaret();
        caretOutput.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

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
                sendMessage(getText());
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
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    sendMessage(getText());
                }
            }
        });
        buttons.add(btnEnviarArquivo, BorderLayout.CENTER);

        JButton btnExit = new JButton("Desconectar");
        btnExit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    disconnect();
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        });
        buttons.add(btnExit, BorderLayout.SOUTH);

        setLocationRelativeTo(null);
        setVisible(true);
        setClientInfo(args[0], args[1], args[2]);
    }

    public void setClientInfo(String user, String serverIP, String serverPort) {
        this.user = user;
        this.serverIP = serverIP;
        this.serverPort = Integer.parseInt(serverPort);
    }

    private void sendMessage(String msg) throws IOException {
        if (msg.equals("Disconnect")) {
            bufferWriter.write(concatMsg("Desconectado"));
        } else {
            bufferWriter.write(concatMsg(msg));
        }
        bufferWriter.flush();
        inputText.setText("");
    }

    private String concatMsg(String msg) {
        return "[" + user + "]" + " -> " + msg + "\r\n";
    }

    private void sendFile() {
        // TODO: send the result to the method that sends the file
        // LIKE: Send(getFile());
        writeOutput(concatMsg("Você enviou o arquivo: " + getFile()));
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

        if (chooseFile.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            file = chooseFile.getSelectedFile().getAbsolutePath();
        }
        return file;
    }

    public void establishConnection() throws IOException {
        socket = new Socket(this.serverIP, this.serverPort);
        outS = socket.getOutputStream();
        outSWriter = new OutputStreamWriter(outS);
        bufferWriter = new BufferedWriter(outSWriter);
        bufferWriter.write(user + "\r\n");
        bufferWriter.flush();
    }

    public void listenConnection() throws IOException {
        InputStream inS = socket.getInputStream();
        InputStreamReader inSReader = new InputStreamReader(inS);
        BufferedReader bufferedReader = new BufferedReader(inSReader);
        String msg = "";

        while (!"Disconnect".equalsIgnoreCase(msg))
            if (bufferedReader.ready()) {
                msg = bufferedReader.readLine();
                if (msg.equals("Disconnect"))
                    writeOutput("Desconectado do servidor...");
                else
                    writeOutput(msg);
            }
    }

    public void disconnect() throws IOException {
        sendMessage("Disconnect");
        outS.close();
        outSWriter.close();
        bufferWriter.close();
        socket.close();
    }
}
