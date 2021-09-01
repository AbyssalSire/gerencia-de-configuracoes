package control;

import java.io.IOException;
import java.sql.Connection;
//import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class UsuarioDAO {
	private Connection connection = null;
	private PreparedStatement pstdados = null;
	private ResultSet rsdados = null;

	private static final String inserir = "INSERT INTO usuarios(nome, nome_usuario, senha) VALUES (?, ?, ?)";
	private static final String consultar = "SELECT id, nome, nome_usuario, senha FROM usuarios";
	private static final String verificaExiste = "SELECT nome_usuario FROM usuarios WHERE nome_usuario=?";
	private static final String login = "SELECT id, nome, nome_usuario, senha FROM usuarios WHERE nome_usuario=?";
	private static final String pesquisaId = "SELECT id, nome, nome_usuario, senha FROM usuarios WHERE id=?";
	private static final String alterar = "UPDATE usuarios SET nome = ?, nome_usuario=?, senha=? where id=? ";
	private static final String excluir = "DELETE FROM usuarios WHERE id = ?";

	public UsuarioDAO() {

	}

	public boolean CriaConexao() {
		try {
			JDBCUtil.init();
			connection = JDBCUtil.getConnection();
			connection.setAutoCommit(false);// configuracao necessaria para confirmacao ou nao de alteracoes no banco de
											// dados.

			// DatabaseMetaData dbmt = connection.getMetaData();

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

	public boolean Inserir(String nome, String nome_usuario, char[] senha, JPanel contentPane) {
		try {
			int tipo = ResultSet.TYPE_SCROLL_SENSITIVE;
			int concorrencia = ResultSet.CONCUR_UPDATABLE;
			pstdados = connection.prepareStatement(verificaExiste, tipo, concorrencia);
			pstdados.setString(1, nome_usuario);
			rsdados = pstdados.executeQuery();
			while (rsdados.next()) {
				if (rsdados.getString("nome_usuario") != "") {
					throw new Exception("Usuário já existe");
				}
			}
			pstdados = null;
			rsdados = null;

			pstdados = connection.prepareStatement(inserir, tipo, concorrencia);
			pstdados.setString(1, nome);
			pstdados.setString(2, nome_usuario);
			pstdados.setString(3, String.valueOf(senha));
			int resposta = pstdados.executeUpdate();
			pstdados.close();
			if (resposta == 1) {
				connection.commit();
				return true;
			} else {
				connection.rollback();
				return false;
			}
		} catch (SQLException erro) {
			System.out.println("Erro na execução da inserção = " + erro);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(contentPane, e.getMessage(), "Mensagem de erro", JOptionPane.ERROR_MESSAGE);
		}
		return false;
	}

	public boolean Consultar() {
		try {
			int tipo = ResultSet.TYPE_SCROLL_SENSITIVE;
			int concorrencia = ResultSet.CONCUR_UPDATABLE;
			pstdados = connection.prepareStatement(consultar, tipo, concorrencia);
			rsdados = pstdados.executeQuery();
			return true;
		} catch (SQLException erro) {
			System.out.println("Erro ao executar consulta = " + erro);
		}
		return false;
	}

	public ResultSet getRsdados() {
		return rsdados;
	}

	public ResultSet getRsdados(int id) {
		try {
			int tipo = ResultSet.TYPE_SCROLL_SENSITIVE;
			int concorrencia = ResultSet.CONCUR_UPDATABLE;
			pstdados = connection.prepareStatement(pesquisaId, tipo, concorrencia);
			pstdados.setInt(1, id);
			rsdados = pstdados.executeQuery();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return rsdados;
	}

	public int Login(String nome_usuario, char[] senha, JPanel contentPane) {
		int id = 0;
		@SuppressWarnings("unused")
		String nome_usuarioConfere;
		String senhaConfere = null;
		try {
			int tipo = ResultSet.TYPE_SCROLL_SENSITIVE;
			int concorrencia = ResultSet.CONCUR_UPDATABLE;
			pstdados = connection.prepareStatement(login, tipo, concorrencia);
			pstdados.setString(1, nome_usuario);
			rsdados = pstdados.executeQuery();

			while (rsdados.next()) {
				id = rsdados.getInt("id");
				nome_usuarioConfere = rsdados.getString("nome_usuario");
				senhaConfere = rsdados.getString("senha");
			}
			if (!Arrays.equals(senhaConfere.toCharArray(), senha)) {
				id = -1;
				throw new Exception("Nome de usuário ou senha incorretos");
			} else
				return id;
		} catch (SQLException erro) {
			JOptionPane.showMessageDialog(contentPane, "Erro no sql", "Mensagem de erro", JOptionPane.ERROR_MESSAGE);
			System.out.println("Erro na execução da atualização = " + erro);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(contentPane, "Nome de usuário ou senha incorretos", "Mensagem de erro",
					JOptionPane.ERROR_MESSAGE);
		}
		return id;
	}

	public boolean Alterar(int id, String nome, String nome_usuario, String senha, JPanel contentPane) {
		try {
			int tipo = ResultSet.TYPE_SCROLL_SENSITIVE;
			int concorrencia = ResultSet.CONCUR_UPDATABLE;
			pstdados = connection.prepareStatement(alterar, tipo, concorrencia);
			pstdados.setString(1, nome);
			pstdados.setString(2, nome_usuario);
			pstdados.setString(3, senha);
			pstdados.setInt(4, id);
			int resposta = pstdados.executeUpdate();
			pstdados.close();
			if (resposta == 1) {
				connection.commit();
				JOptionPane.showMessageDialog(contentPane, "Usuário alterado com sucesso!", "Mensagem de sucesso",
						JOptionPane.INFORMATION_MESSAGE);
				return true;
			} else {
				connection.rollback();
				throw new Exception("Erro ao atualizar");
			}
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(contentPane, "Erro no SQL", "Mensagem de erro", JOptionPane.ERROR_MESSAGE);
			System.out.println("Erro na execução da inserção = " + e.getMessage());
		} catch (Exception e) {
			JOptionPane.showMessageDialog(contentPane, "Erro!", "Mensagem de erro", JOptionPane.ERROR_MESSAGE);
			System.out.println("Erro na execução da inserção = " + e.getMessage());
		}
		return false;
	}

	public boolean Excluir(int id, JPanel contentPane) {
		try {
			int tipo = ResultSet.TYPE_SCROLL_SENSITIVE;
			int concorrencia = ResultSet.CONCUR_UPDATABLE;
			pstdados = connection.prepareStatement(excluir, tipo, concorrencia);
			pstdados.setInt(1, id);
			int resposta = pstdados.executeUpdate();
			pstdados.close();
			if (resposta == 1) {
				connection.commit();
				JOptionPane.showMessageDialog(contentPane, "Removido com sucesso", "Mensagem de sucesso",
						JOptionPane.INFORMATION_MESSAGE);
				return true;
			} else {
				connection.rollback();
				throw new Exception("Erro ao excluir");
			}
		} catch (SQLException erro) {
			System.out.println("Erro na execução da exclusão = " + erro);
			JOptionPane.showMessageDialog(contentPane, erro.getMessage(), "Mensagem de erro",
					JOptionPane.ERROR_MESSAGE);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(contentPane, e.getMessage(), "Mensagem de erro", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
		return false;
	}

}
