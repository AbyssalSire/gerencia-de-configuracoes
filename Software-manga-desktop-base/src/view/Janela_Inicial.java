package view;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import control.JDBCUtil;
import control.UsuarioDAO;

import javax.swing.JPasswordField;
import javax.swing.JButton;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.awt.event.ActionEvent;
import javax.swing.JPanel;

public class Janela_Inicial {

	UsuarioDAO u = new UsuarioDAO();

	Connection con = null;

	public static final String relatorio_fonte_usuario = System.getProperty("user.dir")
			+ "/src/relatorios/relatorio_usuarios_cherry.jrxml";
	public static final String relatorio_fonte_lista = System.getProperty("user.dir")
			+ "/src/relatorios/relatorio_lista_mangas_cherry.jrxml";


	public JFrame frmListaDeMangs;
	private JTextField txtUserName;
	private JPasswordField passwordField;
	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Janela_Inicial window = new Janela_Inicial();
					window.frmListaDeMangs.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 * 
	 * @throws SQLException
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public Janela_Inicial() throws SQLException, ClassNotFoundException, IOException {
		JDBCUtil.init();
		con = JDBCUtil.getConnection();
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmListaDeMangs = new JFrame();
		frmListaDeMangs.setTitle("Lista de Mangás");
		frmListaDeMangs.setBounds(100, 100, 300, 230);
		frmListaDeMangs.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmListaDeMangs.getContentPane().setLayout(null);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(null);

		txtUserName = new JTextField();
		txtUserName.setBounds(123, 56, 150, 20);
		frmListaDeMangs.getContentPane().add(txtUserName);
		txtUserName.setColumns(10);

		passwordField = new JPasswordField();
		passwordField.setBounds(123, 87, 150, 20);
		frmListaDeMangs.getContentPane().add(passwordField);

		JLabel lblNewLabel = new JLabel("Nome de usuário");
		lblNewLabel.setBounds(10, 59, 103, 14);
		frmListaDeMangs.getContentPane().add(lblNewLabel);

		JLabel lblNewLabel_1 = new JLabel("Senha");
		lblNewLabel_1.setBounds(67, 90, 46, 14);
		frmListaDeMangs.getContentPane().add(lblNewLabel_1);

		JButton btnLogin = new JButton("Login");
		btnLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				u.CriaConexao();
				int id = u.Login(txtUserName.getText(), passwordField.getPassword(), contentPane);
				u.FechaConexao();
				if (id > 0) {
					frmListaDeMangs.dispose();
					Janela_Main frame = new Janela_Main(id);
					frame.setVisible(true);
				}
			}
		});
		btnLogin.setBounds(184, 118, 89, 23);
		frmListaDeMangs.getContentPane().add(btnLogin);

		JButton btnCadastrar = new JButton("Cadastrar");
		btnCadastrar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Janela_Cadastro window = new Janela_Cadastro();
				window.frmCadastro.setVisible(true);
			}
		});
		btnCadastrar.setFont(new Font("Tahoma", Font.PLAIN, 11));
		btnCadastrar.setBounds(184, 152, 89, 23);
		frmListaDeMangs.getContentPane().add(btnCadastrar);

		JLabel lblNewLabel_2 = new JLabel("Tela de login");
		lblNewLabel_2.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblNewLabel_2.setBounds(65, 11, 115, 34);
		frmListaDeMangs.getContentPane().add(lblNewLabel_2);


	}
}
