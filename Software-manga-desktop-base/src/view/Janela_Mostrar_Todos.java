package view;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import control.MangaDAO;

import javax.swing.JScrollPane;

import model.Manga;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Janela_Mostrar_Todos extends JFrame {

	MangaDAO m = new MangaDAO();
	Manga manga = null;
	ArrayList<Manga> lista = new ArrayList<Manga>();
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTable table;

	/**
	 * Create the frame.
	 */

	public Janela_Mostrar_Todos(int id) {

		setTitle("Lista de Mangás");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 450, 335);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 11, 414, 274);
		contentPane.add(scrollPane);

		table = new JTable();
		scrollPane.setViewportView(table);
		table.setModel(new DefaultTableModel(new String[] { "Título", "Descrição", "Volumes" }, 0) {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;
			boolean[] columnEditables = new boolean[] { false, false, false };

			public boolean isCellEditable(int row, int column) {
				return columnEditables[column];
			}
		});
		
		m.CriaConexao();
		m.Consultar(id);
		ResultSet r = m.getRsdados();
		try {
			while(r.next()) {
				manga = new Manga(r.getString("nome_manga"), r.getString("descricao_manga"));
				manga.setListaVolumes(r.getString("lista_volumes"));
				lista.add(manga);
				manga = null;
				
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		m.PopularTabela(table, lista);
		table.getColumnModel().getColumn(0).setResizable(false);
		table.getColumnModel().getColumn(1).setResizable(false);
		table.getColumnModel().getColumn(2).setResizable(false);
		m.FechaConexao();

	}
}
