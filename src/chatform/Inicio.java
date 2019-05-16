package chatform;

import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.event.ActionEvent;

public class Inicio extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5602644222765201727L;
	private final JPanel contentPanel = new JPanel();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			Inicio dialog = new Inicio();
			
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public Inicio() {
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(null);
		contentPanel.setBounds(0, 0, 434, 228);
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel);
		contentPanel.setLayout(null);
		setLocationRelativeTo(null);
		setTitle("Aplica��o de Conversa");
		{
			JButton btnServidor = new JButton("Servidor");
			btnServidor.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					Server server = new Server();
					server.setVisible(true);
					Inicio.this.dispose();
				}
			});
			btnServidor.setBounds(10, 126, 182, 61);
			contentPanel.add(btnServidor);
		}
		{
			JButton btnCliente = new JButton("Cliente");
			btnCliente.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					Login login = new Login();
					login.setVisible(true);
					Inicio.this.setEnabled(false);
					WindowListener exitListener = new WindowAdapter() {

					    @Override
					    public void windowClosed(WindowEvent e) {
					        if (login.next == true)
					        {
					        	Inicio.this.setEnabled(true);
					        	Inicio.this.dispose();
					        }
					        else {
					        	Inicio.this.setEnabled(true);
					        	Inicio.this.setVisible(true);
					        }
					    }
					};
					login.addWindowListener(exitListener);
				}
			});
			btnCliente.setBounds(242, 126, 182, 61);
			contentPanel.add(btnCliente);
		}
		{
			JLabel lblInicializar = new JLabel("Como deseja inicializar o programa?");
			lblInicializar.setBounds(97, 40, 242, 34);
			contentPanel.add(lblInicializar);
			lblInicializar.setFont(new Font("Arial", Font.PLAIN, 15));
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setBounds(0, 228, 434, 33);
			getContentPane().add(buttonPane);
			buttonPane.setLayout(null);
		}
	}

}
