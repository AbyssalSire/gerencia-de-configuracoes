package view;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.Font;
import javax.swing.JTextField;

import control.UsuarioDAO;

import javax.swing.JPasswordField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Janela_Cadastro {

	UsuarioDAO u = new UsuarioDAO();
	JFrame frmCadastro;
	private JTextField textFieldNome;
	private JTextField textFieldNomeUsuario;
	private JPasswordField passwordField;


	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Janela_Cadastro window = new Janela_Cadastro();
					window.frmCadastro.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Janela_Cadastro() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmCadastro = new JFrame();
		frmCadastro.setTitle("Cadastro");
		
		frmCadastro.setBounds(100, 100, 250, 245);
		frmCadastro.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frmCadastro.getContentPane().setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBounds(0, 0, 234, 261);
		frmCadastro.getContentPane().add(panel);
		panel.setLayout(null);

		
		JLabel lblNewLabel = new JLabel("Cadastre seu usuário");
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblNewLabel.setBounds(10, 11, 179, 27);
		panel.add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("Nome");
		lblNewLabel_1.setBounds(10, 50, 46, 14);
		panel.add(lblNewLabel_1);
		
		JLabel lblNewLabel_2 = new JLabel("Nome de usuário");
		lblNewLabel_2.setBounds(10, 75, 110, 14);
		panel.add(lblNewLabel_2);
		
		JLabel lblNewLabel_3 = new JLabel("Senha");
		lblNewLabel_3.setBounds(10, 100, 46, 14);
		panel.add(lblNewLabel_3);
		
		textFieldNome = new JTextField();
		textFieldNome.setBounds(120, 50, 100, 20);
		panel.add(textFieldNome);
		textFieldNome.setColumns(10);
		
		textFieldNomeUsuario = new JTextField();
		textFieldNomeUsuario.setBounds(120, 75, 100, 20);
		panel.add(textFieldNomeUsuario);
		textFieldNomeUsuario.setColumns(10);
		
		passwordField = new JPasswordField();
		passwordField.setBounds(120, 100, 100, 20);
		panel.add(passwordField);
		
		JButton btnCadastrar = new JButton("Cadastrar");
		btnCadastrar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				u.CriaConexao();
				boolean funcionou = u.Inserir(textFieldNome.getText(), textFieldNomeUsuario.getText(), passwordField.getPassword(), panel);
				if(funcionou) {
					int id = u.Login(textFieldNomeUsuario.getText(), passwordField.getPassword(), panel);
					u.FechaConexao();
					frmCadastro.dispose();
					Janela_Main frame = new Janela_Main(id);
					frame.setVisible(true);
				}
				
			}
		});
		btnCadastrar.setBounds(120, 130, 100, 23);
		panel.add(btnCadastrar);
		
		JButton btnVoltar = new JButton("Voltar");
		btnVoltar.setBounds(120, 160, 100, 23);
		panel.add(btnVoltar);
	}
}
