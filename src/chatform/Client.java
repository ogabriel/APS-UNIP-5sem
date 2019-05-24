package chatform;

import java.awt.BorderLayout;
import java.awt.event.*;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextField;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JButton;
import javax.swing.ScrollPaneConstants;
import javax.swing.text.DefaultCaret;

import java.io.*;

import java.net.ConnectException;
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
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.addWindowListener(new WindowAdapter() {
                                   @Override
                                   public void windowClosing(WindowEvent e) {
                                       if(socket == null)
                                           dispose();
                                       else if(!socket.isClosed()) {
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
        setTitle("Aplica��o de Conversa (Cliente)");

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
                String text = inputText.getText();
                if(!text.equals("")) {
                    sendMessage(concatMsg(text));
                    inputText.setText("");
                }
            }
        });
        buttons.add(btnSend, BorderLayout.NORTH);

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
                    String text = inputText.getText();
                    if(!text.equals("")) {
                        sendMessage(concatMsg(text));
                        inputText.setText("");
                    }
                }
            }
        });

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

    private void sendMessage(String msg) {
        try {
            bufferWriter.write(msg);
            bufferWriter.flush();
        } catch (Exception e) {
            writeOutput("Desconectado");
        }
    }

    private String concatMsg(String msg) {
        return "Text&" + "[" + user + "]" + " ~> " + msg + "\r\n";
    }

    private void writeOutput(String phrase) {
        output.append(phrase + "\r\n");
    }

    public void establishConnection() throws IOException {
        try {
            socket = new Socket(this.serverIP, this.serverPort);
            outS = socket.getOutputStream();
            outSWriter = new OutputStreamWriter(outS);
            bufferWriter = new BufferedWriter(outSWriter);
            bufferWriter.write(user + "\r\n");
            bufferWriter.flush();
        } catch (ConnectException e) {
            System.out.println("Nao foi possível criar a conexao, servidor indiponível na porta e ip indicados");
        } catch (Exception e) {
            e.printStackTrace();
            disconnect();
        }
    }

    public void listenConnection() throws IOException {
        try {
            InputStream inS = socket.getInputStream();
            InputStreamReader inSReader = new InputStreamReader(inS);
            BufferedReader bufferedReader = new BufferedReader(inSReader);
            String msg = "";

            while (!("Disconnect " + user).equalsIgnoreCase(msg))
                if (bufferedReader.ready()) {
                    msg = bufferedReader.readLine();
                    if (msg.equals("Disconnect"))
                        writeOutput("Desconectado do servidor...");
                    else
                        writeOutput(msg);
                }
        } catch (Exception e) {
            System.out.println("Impossível escutar servidor. O mesmo possívelmente esta indisponível");
        }
    }

    public void disconnect() throws IOException {
        sendMessage("Disconnect " + this.user);
        try {
            outS.close();
            outSWriter.close();
            bufferWriter.close();
            socket.close();
        } catch (Exception e) {
            System.out.println("Nao é possível fechar conexao");
        }
    }
}
