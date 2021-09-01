package view;

import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JFrame;
import javax.swing.JPanel;
import control.MangaDAO;
import control.UsuarioDAO;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.SwingConstants;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JTextField;
import java.awt.Color;

public class Janela_Usuario {

	JFrame frmTelaUsurio;
	UsuarioDAO u = new UsuarioDAO();
	MangaDAO m = new MangaDAO();
	private JTextField textFieldNome;
	private JTextField textFieldNomeUsuario;
	private JTextField textFieldSenha;

	/**
	 * Create the application.
	 */
	public Janela_Usuario(int id) {
		initialize(id);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize(int id) {
		String nome = null;
		String nome_usuario = null;
		String senha = null;
		try {
			u.CriaConexao();
			u.Consultar();
			ResultSet r = u.getRsdados(id);
			u.FechaConexao();
			while (r.next()) {
				nome = r.getString("nome");
				nome_usuario = r.getString("nome_usuario");
				senha = r.getString("senha");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		frmTelaUsurio = new JFrame();
		frmTelaUsurio.setTitle("Conta de" + nome);
		frmTelaUsurio.setBounds(100, 100, 450, 300);
		frmTelaUsurio.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frmTelaUsurio.getContentPane().setLayout(null);

		JPanel panel = new JPanel();
		panel.setBounds(0, 0, 434, 261);
		frmTelaUsurio.getContentPane().add(panel);
		panel.setLayout(null);

		JLabel lblDadosDaConta = new JLabel("Dados da Conta");
		lblDadosDaConta.setHorizontalAlignment(SwingConstants.CENTER);
		lblDadosDaConta.setFont(new Font("Tahoma", Font.BOLD, 18));
		lblDadosDaConta.setBounds(118, 11, 181, 38);
		panel.add(lblDadosDaConta);

		JLabel lblNome = new JLabel("Nome:");
		lblNome.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblNome.setBounds(10, 70, 52, 23);
		panel.add(lblNome);

		JLabel lblNomeUsuario = new JLabel("Nome de Usuário:");
		lblNomeUsuario.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblNomeUsuario.setBounds(10, 108, 119, 23);
		panel.add(lblNomeUsuario);

		JLabel lblSenha = new JLabel("Senha:");
		lblSenha.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblSenha.setBounds(10, 146, 59, 23);
		panel.add(lblSenha);

		JButton btnAlterar = new JButton("Alterar");
		btnAlterar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				u.CriaConexao();
				u.Alterar(id, textFieldNome.getText(), textFieldNomeUsuario.getText(), textFieldSenha.getText(), panel);
				u.FechaConexao();
			}
		});
		btnAlterar.setFont(new Font("Tahoma", Font.BOLD, 16));
		btnAlterar.setBounds(183, 218, 142, 32);
		panel.add(btnAlterar);

		JButton btnVoltar = new JButton("Voltar");
		btnVoltar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frmTelaUsurio.dispose();
			}
		});
		btnVoltar.setFont(new Font("Tahoma", Font.BOLD, 16));
		btnVoltar.setBounds(335, 218, 89, 32);
		panel.add(btnVoltar);

		textFieldNome = new JTextField();
		textFieldNome.setBounds(72, 70, 208, 23);
		panel.add(textFieldNome);
		textFieldNome.setColumns(10);
		textFieldNome.setText(nome);

		textFieldNomeUsuario = new JTextField();
		textFieldNomeUsuario.setColumns(10);
		textFieldNomeUsuario.setBounds(139, 108, 208, 23);
		panel.add(textFieldNomeUsuario);
		textFieldNomeUsuario.setText(nome_usuario);

		textFieldSenha = new JTextField();
		textFieldSenha.setColumns(10);
		textFieldSenha.setBounds(72, 150, 208, 23);
		panel.add(textFieldSenha);
		textFieldSenha.setText(senha);

		JButton btnRemover = new JButton("Remover");
		btnRemover.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				m.CriaConexao();
				u.CriaConexao();
				m.ExcluirUsuario(id, panel);
				m.FechaConexao();
				u.Excluir(id, panel);
				u.FechaConexao();
				frmTelaUsurio.dispose();
				System.exit(0);

			}
		});
		btnRemover.setBackground(new Color(255, 0, 0));
		btnRemover.setFont(new Font("Tahoma", Font.BOLD, 14));
		btnRemover.setBounds(10, 218, 119, 32);
		panel.add(btnRemover);
	}

}
