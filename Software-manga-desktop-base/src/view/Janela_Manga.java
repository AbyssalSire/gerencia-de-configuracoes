package view;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import java.awt.Font;
import javax.swing.JTextArea;
import javax.swing.JButton;
import javax.swing.border.TitledBorder;

import control.MangaDAO;
import model.Manga;

import javax.swing.JScrollPane;
import javax.swing.JTextField;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.JTextPane;
import java.awt.Color;

public class Janela_Manga extends JFrame {
	MangaDAO m = new MangaDAO();

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField volumeAdicionar;

	/**
	 * Create the frame.
	 */
	public Janela_Manga(int id, Manga manga) {

		setTitle(manga.getTitulo());
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 450, 480);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lblTitulo = new JLabel(manga.getTitulo());
		lblTitulo.setFont(new Font("Tahoma", Font.BOLD, 20));
		lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
		lblTitulo.setBounds(4, 11, 414, 30);
		contentPane.add(lblTitulo);

		JPanel panelDescricao = new JPanel();
		panelDescricao
				.setBorder(new TitledBorder(null, "Descrição", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panelDescricao.setBounds(4, 52, 426, 195);
		contentPane.add(panelDescricao);
		panelDescricao.setLayout(null);

		JPanel panel = new JPanel();
		panel.setBounds(10, 15, 406, 135);
		panelDescricao.add(panel);
		panel.setLayout(null);

		JTextArea txtDescricao = new JTextArea();
		txtDescricao.setBounds(0, 0, 406, 135);
		panel.add(txtDescricao);
		txtDescricao.setLineWrap(true);
		txtDescricao.setText(manga.getDescricao());
		txtDescricao.setFont(new Font("Tahoma", Font.PLAIN, 13));

		JButton btnAlterarDescricao = new JButton("Alterar descrição");
		btnAlterarDescricao.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				m.CriaConexao();
				// int id, String nome_manga, String descricao_manga, JPanel contentPane
				m.AlterarDescricao(id, manga.getTitulo(), txtDescricao.getText(), contentPane);
				m.FechaConexao();
			}
		});
		btnAlterarDescricao.setBounds(266, 161, 150, 23);
		panelDescricao.add(btnAlterarDescricao);

		JPanel panelListaVolume = new JPanel();
		panelListaVolume.setBorder(
				new TitledBorder(null, "Lista de Volumes", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panelListaVolume.setBounds(4, 258, 426, 139);
		contentPane.add(panelListaVolume);
		panelListaVolume.setLayout(null);

		volumeAdicionar = new JTextField();
		volumeAdicionar.setBounds(180, 11, 45, 45);
		volumeAdicionar.setColumns(10);
		panelListaVolume.add(volumeAdicionar);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 67, 406, 52);
		panelListaVolume.add(scrollPane);

		JTextPane textListaVolumes = new JTextPane();
		if (manga.getListaVolumes2() == null) {
			textListaVolumes.setText("");
		} else
			textListaVolumes.setText(manga.getListaVolumes2().toString());
		textListaVolumes.setEditable(false);
		scrollPane.setViewportView(textListaVolumes);

		JButton btnAdicionaVolume = new JButton("+");
		// (Manga manga, int id, String valor, JPanel contentPane)
		btnAdicionaVolume.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				m.CriaConexao();
				m.AdicionarVolume(manga, id, volumeAdicionar.getText(), contentPane);
				m.FechaConexao();
				volumeAdicionar.setText("");
				manga.DesconverterListaVolumes();
				System.out.println(manga.getListaVolumes2().toString());
				textListaVolumes.setText(manga.getListaVolumes2().toString());
			}
		});
		btnAdicionaVolume.setBounds(235, 11, 45, 45);
		btnAdicionaVolume.setFont(new Font("Tahoma", Font.BOLD, 14));
		panelListaVolume.add(btnAdicionaVolume);

		JButton btnRemoveVolume = new JButton("-");
		btnRemoveVolume.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				m.CriaConexao();
				m.RemoverVolume(manga, id, volumeAdicionar.getText(), contentPane);
				volumeAdicionar.setText("");
				manga.DesconverterListaVolumes();
				textListaVolumes.setText(manga.getListaVolumes2().toString());
				m.FechaConexao();
			}
		});
		btnRemoveVolume.setBounds(125, 11, 45, 45);
		btnRemoveVolume.setFont(new Font("Tahoma", Font.BOLD, 15));
		panelListaVolume.add(btnRemoveVolume);

		JButton btnApagar = new JButton("Remover mangá da lista");
		btnApagar.setFont(new Font("Tahoma", Font.BOLD, 13));
		btnApagar.setBackground(Color.RED);
		btnApagar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				m.CriaConexao();
				m.Excluir(manga, id, contentPane);
				m.FechaConexao();
				dispose();
			}
		});
		btnApagar.setBounds(121, 406, 203, 25);
		contentPane.add(btnApagar);
	}
}
