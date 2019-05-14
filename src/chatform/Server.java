package chatform;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.CardLayout;

public class Server extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4998717362394143017L;
	private JPanel contentPane;
	private JTextField numeroPorta;
	private JButton btnOk;
	private JButton btnVoltar;
	private JPanel panelConfig;
	private JPanel panelStatus;
	private JLabel lblIp;
	private JLabel lblPorta;
	private JLabel lblValorIp;
	private JLabel lblValorPorta;
	private JButton btnEncerrarConexao;
	public boolean a;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Server frame = new Server();
					frame.setTitle("Aplicação de Conversa (Servidor)");
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
	public Server() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 366, 158);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new CardLayout(0, 0));
		this.setLocationRelativeTo(null);
		this.setTitle("Aplicação de Conversa (Servidor)");
		
		panelConfig = new JPanel();
		contentPane.add(panelConfig, "panelConfig");
		panelConfig.setLayout(null);
		
		JLabel lblNumeroDaPorta = new JLabel("N\u00FAmero da porta:");
		lblNumeroDaPorta.setBounds(10, 35, 113, 18);
		panelConfig.add(lblNumeroDaPorta);
		lblNumeroDaPorta.setFont(new Font("Arial", Font.PLAIN, 15));
		
		numeroPorta = new JTextField();
		numeroPorta.setBounds(135, 35, 86, 20);
		panelConfig.add(numeroPorta);
		numeroPorta.setText("12345");
		numeroPorta.setColumns(10);
		
		btnOk = new JButton("Ok");
		btnOk.setBounds(90, 86, 73, 23);
		panelConfig.add(btnOk);
		btnOk.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				CardLayout c = (CardLayout)(contentPane.getLayout());
				c.show(contentPane, "panelStatus");
			}
		});
		
		btnVoltar = new JButton("Voltar");
		btnVoltar.setBounds(177, 86, 73, 23);
		panelConfig.add(btnVoltar);
		
		panelStatus = new JPanel();
		contentPane.add(panelStatus, "panelStatus");
		panelStatus.setLayout(null);
		
		lblIp = new JLabel("IP:");
		lblIp.setFont(new Font("Arial", Font.PLAIN, 15));
		lblIp.setBounds(38, 11, 46, 14);
		panelStatus.add(lblIp);
		
		lblPorta = new JLabel("Porta:");
		lblPorta.setFont(new Font("Arial", Font.PLAIN, 15));
		lblPorta.setBounds(38, 36, 46, 14);
		panelStatus.add(lblPorta);
		
		lblValorIp = new JLabel(); //TODO: JLabel(getIP())
		lblValorIp.setFont(new Font("Tahoma", Font.BOLD, 13));
		lblValorIp.setBounds(67, 11, 171, 14);
		panelStatus.add(lblValorIp);
		
		lblValorPorta = new JLabel(); //TODO: JLabel(getPort())
		lblValorPorta.setFont(new Font("Tahoma", Font.BOLD, 13));
		lblValorPorta.setBounds(80, 36, 154, 14);
		panelStatus.add(lblValorPorta);
		
		btnEncerrarConexao = new JButton("Encerrar Conex\u00E3o");
		btnEncerrarConexao.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) { 
				//TODO: Close connection. Maybe dispose() instead of exit() in case Connection can be reestablished
				System.exit(0);
			}
		});
		btnEncerrarConexao.setBounds(94, 79, 144, 30);
		panelStatus.add(btnEncerrarConexao);
		btnVoltar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Server.this.dispose();
				Inicio inicio = new Inicio();
				inicio.setVisible(true);
			}
		});
	}
}
