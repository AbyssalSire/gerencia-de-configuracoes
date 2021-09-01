package control;

import java.awt.Color;
import java.awt.Font;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import model.Manga;

public class MangaDAO {
	private Connection connection = null;
	private PreparedStatement pstdados = null;
	private ResultSet rsdados = null;

	private static final String inserir = "INSERT INTO lista_mangas(nome_manga, id_usuario, descricao_manga, lista_volumes)VALUES (?, ?, ?, ?)";
	private static final String consultar = "SELECT nome_manga, descricao_manga, lista_volumes FROM lista_mangas WHERE id_usuario = ?;";
	private static final String pesquisar = "SELECT nome_manga, descricao_manga, lista_volumes FROM lista_mangas WHERE id_usuario = ? AND nome_manga LIKE ?;";
	// inserir '%' ? '%' na pesquisa do prepared statemente
	private static final String consultarExiste = "SELECT nome_manga, descricao_manga, lista_volumes FROM lista_mangas WHERE nome_manga LIKE ? AND id_usuario = ?;";
	// private static final String alterar = "UPDATE lista_mangas SET nome_manga=?,
	// descricao_manga=?, lista_volumes=? WHERE id_usuario = ?;";
	private static final String alterarDescricao = "UPDATE lista_mangas SET descricao_manga=? WHERE id_usuario = ? AND nome_manga=?;";
	private static final String alterarListaManga = "UPDATE lista_mangas SET lista_volumes=? WHERE nome_manga=? AND id_usuario=?;";
	private static final String excluir = "DELETE FROM lista_mangas WHERE id_usuario=? AND nome_manga = ?;";
	private static final String excluirUsuario = "DELETE FROM lista_mangas WHERE id_usuario=?;";

	public MangaDAO() {

	}

	public boolean CriaConexao() {
		try {
			JDBCUtil.init();
			connection = JDBCUtil.getConnection();
			connection.setAutoCommit(false);// configuracao necessaria para confirmacao ou nao de alteracoes no banco de
											// dados.

			return true;
		} catch (ClassNotFoundException erro) {
			System.out.println("Falha ao carregar o driver JDBC." + erro);
		} catch (IOException erro) {
			System.out.println("Falha ao carregar o arquivo de configuração." + erro);
		} catch (SQLException erro) {
			System.out.println("Falha na conexao, comando sql = " + erro);
		}
		return false;
	}

	public boolean FechaConexao() {
		if (connection != null) {
			try {
				connection.close();
				return true;
			} catch (SQLException erro) {
				System.err.println("Erro ao fechar a conexão = " + erro);
				return false;
			}
		} else {
			return false;
		}
	}

	public boolean Inserir(int id, String nome_manga, String descricao_manga, String lista_volumes,
			JPanel contentPane) {
		try {
			int tipo = ResultSet.TYPE_SCROLL_SENSITIVE;
			int concorrencia = ResultSet.CONCUR_UPDATABLE;
			pstdados = connection.prepareStatement(consultarExiste, tipo, concorrencia);
			pstdados.setString(1, "%" + nome_manga + "%");
			pstdados.setInt(2, id);
			rsdados = pstdados.executeQuery();
			while (rsdados.next()) {
				if (rsdados.getString("nome_manga") != "") {
					throw new Exception("Mangá já existe");
				}
			}
			pstdados = null;
			rsdados = null;

			pstdados = connection.prepareStatement(inserir, tipo, concorrencia);
			pstdados.setString(1, nome_manga);
			pstdados.setInt(2, id);
			pstdados.setString(3, descricao_manga);
			pstdados.setString(4, lista_volumes);
			int resposta = pstdados.executeUpdate();
			pstdados.close();
			if (resposta == 1) {
				connection.commit();
				JOptionPane.showMessageDialog(contentPane, "Mangá adicionado com sucesso", "Mensagem de sucesso",
						JOptionPane.INFORMATION_MESSAGE);
				return true;
			} else {
				connection.rollback();
				return false;
			}
		} catch (SQLException erro) {
			JOptionPane.showMessageDialog(contentPane, "Erro no SQL", "Mensagem de erro", JOptionPane.ERROR_MESSAGE);
			System.out.println("Erro na execução da inserção = " + erro.getMessage());
		} catch (Exception e) {
			System.out.println(e.getMessage());
			JOptionPane.showMessageDialog(contentPane, "Título já adicionado à lista", "Mensagem de erro",
					JOptionPane.ERROR_MESSAGE);
		}
		return false;
	}

	public boolean Consultar(int id) {
		try {
			int tipo = ResultSet.TYPE_SCROLL_SENSITIVE;
			int concorrencia = ResultSet.CONCUR_UPDATABLE;
			pstdados = connection.prepareStatement(consultar, tipo, concorrencia);
			pstdados.setInt(1, id);
			rsdados = pstdados.executeQuery();
			return true;
		} catch (SQLException erro) {
			System.out.println(erro.getMessage());
			System.out.println("Erro ao executar consulta = " + erro);
		}
		return false;
	}

	public Manga Pesquisa(int id, String nome_manga, JPanel contentPane) {
		Manga m = null;
		try {
			int tipo = ResultSet.TYPE_SCROLL_SENSITIVE;
			int concorrencia = ResultSet.CONCUR_UPDATABLE;
			pstdados = connection.prepareStatement(pesquisar, tipo, concorrencia);
			pstdados.setInt(1, id);
			pstdados.setString(2, "%" + nome_manga + "%");
			rsdados = pstdados.executeQuery();
			while (rsdados.next()) {
				m = new Manga(rsdados.getString("nome_manga"), rsdados.getString("descricao_manga"));
				m.setListaVolumes(rsdados.getString("lista_volumes"));
			}
			return m;
		} catch (SQLException erro) {
			System.out.println(erro.getMessage());
			JOptionPane.showMessageDialog(contentPane, "Manga nao encontrado", "Mensagem de erro",
					JOptionPane.ERROR_MESSAGE);
		}
		return m;
	}

	public ResultSet getRsdados() {
		return rsdados;
	}

	// alterarDescricao = "UPDATE lista_mangas SET descricao_manga = ? WHERE
	// id_usuario = ? AND nome_manga=?;";
	public boolean AlterarDescricao(int id, String nome_manga, String descricao_manga, JPanel contentPane) {
		try {
			int tipo = ResultSet.TYPE_SCROLL_SENSITIVE;
			int concorrencia = ResultSet.CONCUR_UPDATABLE;
			pstdados = connection.prepareStatement(alterarDescricao, tipo, concorrencia);
			pstdados.setString(1, descricao_manga);
			pstdados.setInt(2, id);
			pstdados.setString(3, nome_manga);

			int resposta = pstdados.executeUpdate();
			pstdados.close();
			if (resposta == 1) {
				JOptionPane.showMessageDialog(contentPane, "Descrição alterada com sucesso", "Mensagem de sucesso",
						JOptionPane.INFORMATION_MESSAGE);
				connection.commit();
				return true;
			} else {
				connection.rollback();
				return false;
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			JOptionPane.showMessageDialog(contentPane, e.getStackTrace(), "Mensagem de erro",
					JOptionPane.ERROR_MESSAGE);
		} catch (Exception e) {
			System.out.println(e.getMessage());
			JOptionPane.showMessageDialog(contentPane, e.getStackTrace(), "Mensagem de erro",
					JOptionPane.ERROR_MESSAGE);
		}
		return false;
	}

	// "UPDATE lista_mangas SET lista_volumes=? WHERE WHERE id_usuario = ? AND
	// nome_manga = ?;";
	public void AdicionarVolume(Manga manga, int id, String valor, JPanel contentPane) {

		try {
			int tipo = ResultSet.TYPE_SCROLL_SENSITIVE;
			int concorrencia = ResultSet.CONCUR_UPDATABLE;
			pstdados = connection.prepareStatement(alterarListaManga, tipo, concorrencia);
			if (manga.getListaVolumes() == null || manga.getListaVolumes2() == null) {
				pstdados.setString(1, valor);
			} else {
				manga.ConverterListaVolumes(manga.getListaVolumes2().toString());
				ArrayList<Integer> listaEnvia = manga.getListaVolumes();
				if (listaEnvia.contains(Integer.parseInt(valor)))
					throw new Exception("volume já existe");
				listaEnvia.add(Integer.parseInt(valor));
				Collections.sort(listaEnvia);
				manga.DesconverterListaVolumes();
				pstdados.setString(1, manga.getListaVolumes2().toString());
			}

			pstdados.setString(2, manga.getTitulo());
			pstdados.setInt(3, id);
			int resposta = pstdados.executeUpdate();
			System.out.println("ok");
			pstdados.close();
			if (resposta == 1) {
				JOptionPane.showMessageDialog(contentPane, "Adicionado com sucesso", "Mensagem de sucesso",
						JOptionPane.INFORMATION_MESSAGE);
				connection.commit();
			} else {
				connection.rollback();
				throw new Exception();
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			JOptionPane.showMessageDialog(contentPane, e.getStackTrace(), "Mensagem de erro",
					JOptionPane.ERROR_MESSAGE);
		} catch (NumberFormatException e) {
			System.out.println(e.getMessage());
			JOptionPane.showMessageDialog(contentPane, "Apenas números por favor", "Mensagem de erro",
					JOptionPane.ERROR_MESSAGE);
		} catch (Exception e) {
			System.out.println(e.getMessage());
			JOptionPane.showMessageDialog(contentPane, "Numero já adicionado", "Mensagem de erro",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	public void RemoverVolume(Manga manga, int id, String valor, JPanel contentPane) {
		try {
			int tipo = ResultSet.TYPE_SCROLL_SENSITIVE;
			int concorrencia = ResultSet.CONCUR_UPDATABLE;
			pstdados = connection.prepareStatement(alterarListaManga, tipo, concorrencia);
			manga.ConverterListaVolumes(manga.getListaVolumes2().toString());
			ArrayList<Integer> listaEnvia = manga.getListaVolumes();
			if (!listaEnvia.contains(Integer.parseInt(valor)))
				throw new Exception("Volume nao existe");
			listaEnvia.remove(Integer.valueOf(Integer.parseInt(valor)));
			Collections.sort(listaEnvia);
			manga.DesconverterListaVolumes();
			pstdados.setString(1, manga.getListaVolumes2().toString());
			pstdados.setString(2, manga.getTitulo());
			pstdados.setInt(3, id);
			int resposta = pstdados.executeUpdate();
			pstdados.close();
			if (resposta == 1) {
				JOptionPane.showMessageDialog(contentPane, "Removido com sucesso", "Mensagem de sucesso",
						JOptionPane.INFORMATION_MESSAGE);
				connection.commit();
			} else {
				connection.rollback();
				throw new Exception();
			}
		} catch (SQLException e) {
			System.err.println(e.getMessage());
			JOptionPane.showMessageDialog(contentPane, e.getStackTrace(), "Mensagem de erro",
					JOptionPane.ERROR_MESSAGE);
		} catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(contentPane, "Apenas números por favor", "Mensagem de erro",
					JOptionPane.ERROR_MESSAGE);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(contentPane, "Volume não está na lista", "Mensagem de erro",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	public boolean Excluir(Manga manga, int id, JPanel contentPane) {
		try {
			int tipo = ResultSet.TYPE_SCROLL_SENSITIVE;
			int concorrencia = ResultSet.CONCUR_UPDATABLE;
			pstdados = connection.prepareStatement(excluir, tipo, concorrencia);
			pstdados.setInt(1, id);
			pstdados.setString(2, manga.getTitulo());
			int resposta = pstdados.executeUpdate();
			pstdados.close();
			if (resposta == 1) {
				connection.commit();
				return true;
			} else {
				connection.rollback();
				return false;
			}
		} catch (Exception e) {
			System.err.println(e.getStackTrace());
			JOptionPane.showMessageDialog(contentPane, e.getStackTrace(), "Mensagem de erro",
					JOptionPane.ERROR_MESSAGE);
		}
		return false;
	}
//"DELETE FROM lista_mangas WHERE id_usuario=?;";
	public boolean ExcluirUsuario(int id, JPanel contentPane) {
		try {
			int tipo = ResultSet.TYPE_SCROLL_SENSITIVE;
			int concorrencia = ResultSet.CONCUR_UPDATABLE;
			pstdados = connection.prepareStatement(excluirUsuario, tipo, concorrencia);
			pstdados.setInt(1, id);
			int resposta = pstdados.executeUpdate();
			pstdados.close();
			if (resposta == 1) {
				connection.commit();
				return true;
			} else {
				connection.rollback();
				return false;
			}
		} catch (Exception e) {
			System.err.println(e.getStackTrace());
			JOptionPane.showMessageDialog(contentPane, e.getStackTrace(), "Mensagem de erro",
					JOptionPane.ERROR_MESSAGE);
		}
		return false;
	}

	public void PopularTabela(JTable table, ArrayList<Manga> lista) {
		DefaultTableModel model = (DefaultTableModel) table.getModel();

		Object rowData[] = new Object[3];
		for (int i = 0; i < lista.size(); i++) {
			rowData[0] = lista.get(i).getTitulo();
			rowData[1] = lista.get(i).getDescricao();
			if (lista.get(i).getListaVolumes2() != null) {
				lista.get(i).ConverterListaVolumes(lista.get(i).getListaVolumes2().toString());
				rowData[2] = lista.get(i).getListaVolumes();
			} else {
				rowData[2] = "0";
			}
			model.addRow(rowData);
		}
	}

	public void resetaTitulo(JTextField txtTitulo, boolean focus) {
		if (focus) {
			txtTitulo.setText("");
			txtTitulo.setForeground(Color.black);
			txtTitulo.setFont(new Font("Tahoma", Font.PLAIN, 11));
		} else {
			txtTitulo.setForeground(Color.gray);
			txtTitulo.setFont(new Font("Tahoma", Font.ITALIC, 11));
			txtTitulo.setText("Insira título aqui...");
		}
	}

	public void resetaDescricao(JTextArea textAreaDescricao, boolean focus) {
		if (focus) {
			textAreaDescricao.setText("");
			textAreaDescricao.setForeground(Color.BLACK);
			textAreaDescricao.setFont(new Font("Tahoma", Font.PLAIN, 11));
		} else {
			textAreaDescricao.setForeground(Color.GRAY);
			textAreaDescricao.setFont(new Font("Tahoma", Font.ITALIC, 11));
			textAreaDescricao.setText("Insira a descrição aqui");
		}

	}

}
