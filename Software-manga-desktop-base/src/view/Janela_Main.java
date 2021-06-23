package view;

import java.awt.Color;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import control.JDBCUtil;
import control.MangaDAO;
import control.UsuarioDAO;
import model.Manga;

import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JTextArea;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.view.JasperViewer;

public class Janela_Main extends JFrame {

	public static final String relatorio_usuario_ativo = System.getProperty("user.dir")
			+ "/src/relatorios/relatorio_usuario_ativo.jrxml";
	
	Connection con = null;

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	UsuarioDAO u = new UsuarioDAO();
	MangaDAO m = new MangaDAO();
	int excluido = 0;

	private JPanel contentPane;
	private JTextField txtTituloPesquisa;
	private JTextField txtTituloAdicao;

	/**
	 * Create the frame.
	 */
	public Janela_Main(int id) {
		try {
			JDBCUtil.init();
			con = JDBCUtil.getConnection();
		} catch (ClassNotFoundException | IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		setResizable(false);
		setTitle("Lista de Mangás");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 290, 375);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JButton btnIrParaAdicionar = new JButton("Adicionar mangá");
		btnIrParaAdicionar.setBounds(110, 312, 150, 23);
		contentPane.add(btnIrParaAdicionar);

		JLabel lblPesquisa = new JLabel("Pesquisa por título");
		lblPesquisa.setFont(new Font("Tahoma", Font.BOLD, 15));
		lblPesquisa.setBounds(10, 0, 150, 30);
		contentPane.add(lblPesquisa);
		
		txtTituloPesquisa = new JTextField();
		txtTituloPesquisa.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				if (txtTituloPesquisa.getText().equals("Insira título aqui...")) {
					txtTituloPesquisa.setText("");
					txtTituloPesquisa.setForeground(Color.black);
					txtTituloPesquisa.setFont(new Font("Tahoma", Font.PLAIN, 11));
				}
			}

			@Override
			public void focusLost(FocusEvent e) {
				if (txtTituloPesquisa.getText().equals("")) {
					txtTituloPesquisa.setForeground(Color.gray);
					txtTituloPesquisa.setFont(new Font("Tahoma", Font.ITALIC, 11));
					txtTituloPesquisa.setText("Insira título aqui...");
				}
			}
		});
		txtTituloPesquisa.setText("Insira título aqui...");

		txtTituloPesquisa.setToolTipText("");
		txtTituloPesquisa.setFont(new Font("Tahoma", Font.ITALIC, 11));
		txtTituloPesquisa.setBounds(10, 30, 150, 20);
		contentPane.add(txtTituloPesquisa);
		txtTituloPesquisa.setColumns(10);

		JButton btnPesquisar = new JButton("Pesquisar");
		btnPesquisar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				m.CriaConexao();
				Manga mangaDaPesquisa = m.Pesquisa(id, txtTituloPesquisa.getText(), contentPane);
				m.FechaConexao();
				if (mangaDaPesquisa != null) {
					new Janela_Manga(id, mangaDaPesquisa).setVisible(true);
				}
				else {
					JOptionPane.showMessageDialog(contentPane, "Nenhum manga encontrado", "Mensagem de erro",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		btnPesquisar.setBounds(160, 29, 100, 22);
		contentPane.add(btnPesquisar);

		JLabel lblAdicionar = new JLabel("Adicionar novo mangá à lista");
		lblAdicionar.setFont(new Font("Tahoma", Font.BOLD, 15));
		lblAdicionar.setBounds(10, 129, 239, 30);
		contentPane.add(lblAdicionar);

		txtTituloAdicao = new JTextField();
		txtTituloAdicao.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				if (txtTituloAdicao.getText().equals("Insira título aqui...")) {
					m.resetaTitulo(txtTituloAdicao, true);
				}
			}

			@Override
			public void focusLost(FocusEvent e) {
				if (txtTituloAdicao.getText().equals("")) {
					m.resetaTitulo(txtTituloAdicao, false);
				}
			}
		});
		txtTituloAdicao.setFont(new Font("Tahoma", Font.ITALIC, 11));
		txtTituloAdicao.setText("Insira título aqui...");
		txtTituloAdicao.setBounds(10, 170, 250, 20);
		contentPane.add(txtTituloAdicao);
		txtTituloAdicao.setColumns(10);

		JButton btnMostrarTodos = new JButton("Mostrar todos os mangás");

		btnMostrarTodos.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				new Janela_Mostrar_Todos(id).setVisible(true);
			}
		});

		btnMostrarTodos.setBounds(70, 60, 190, 23);
		contentPane.add(btnMostrarTodos);

		JTextArea textAreaDescricao = new JTextArea();
		textAreaDescricao.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				if (textAreaDescricao.getText().equals("Insira a descrição aqui")) {
					m.resetaDescricao(textAreaDescricao, true);
				}
			}

			@Override
			public void focusLost(FocusEvent e) {
				if (textAreaDescricao.getText() == "" || textAreaDescricao.getText() == "Insira a descrição aqui") {
					m.resetaDescricao(textAreaDescricao, false);
				}
			}
		});
		textAreaDescricao.setFont(new Font("Tahoma", Font.ITALIC, 11));
		textAreaDescricao.setLineWrap(true);
		textAreaDescricao.setText("Insira a descrição aqui");
		textAreaDescricao.setBounds(10, 201, 250, 100);
		contentPane.add(textAreaDescricao);
		
		JButton btnPerfil = new JButton("Ver perfil");
		btnPerfil.setBackground(new Color(248, 248, 255));
		btnPerfil.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Janela_Usuario window = new Janela_Usuario(id);
				window.frmTelaUsurio.setVisible(true);
			}
		});
		btnPerfil.setBounds(160, 6, 100, 23);
		contentPane.add(btnPerfil);
		
		JButton btnRelatorioUsuarioAtivo = new JButton("Mostrar todos os mangás em forma de relatório");
		btnRelatorioUsuarioAtivo.setFont(new Font("Tahoma", Font.PLAIN, 10));
		btnRelatorioUsuarioAtivo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JasperPrint impressao;
				try {
					Map parametro = new HashMap();
					parametro.put("id_usuario", id);

					// caso seja necessário, compila o relatório (caso deseja usar o jrxml)
					JasperReport JR_compilado = JasperCompileManager.compileReport(relatorio_usuario_ativo);

					// preenchimento do relatorio com a conexao e parametros
					impressao = JasperFillManager.fillReport(JR_compilado, parametro, con);

					// opcao de visualizar o relatorio
					JasperViewer.viewReport(impressao, false);

				} catch (JRException erro) {
					System.err.println("Não foi possível exportar o relatório.\n\n" + erro);
				}
			}
		});
		btnRelatorioUsuarioAtivo.setBounds(10, 94, 250, 23);
		
		contentPane.add(btnRelatorioUsuarioAtivo);
		btnIrParaAdicionar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (txtTituloAdicao.getText().equals("") || txtTituloAdicao.getText().equals("Insira título aqui...")) {
					JOptionPane.showMessageDialog(contentPane, "Área de título é obrigatória", "Mensagem de erro",
							JOptionPane.ERROR_MESSAGE);
				} else {
					if (textAreaDescricao.getText() == "" || textAreaDescricao.getText() == "Insira a descrçãoo aqui") {
						textAreaDescricao.setText("sem descrição");
					}
					m.CriaConexao();
					m.Inserir(id, txtTituloAdicao.getText(), textAreaDescricao.getText(), null, contentPane);
					m.FechaConexao();
					m.resetaTitulo(txtTituloAdicao, false);
					m.resetaDescricao(textAreaDescricao, false);
				}
			}
		});
	}
}
