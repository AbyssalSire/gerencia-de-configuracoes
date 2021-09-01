package model;

import java.io.Serializable;
import java.util.ArrayList;

public class Usuario implements Serializable {
	private static final long serialVersionUID = 1L;

	String usuario;
	String nome;
	String senha;
	ArrayList<Manga> lista;

	public Usuario(String userName, String nome, String senha, ArrayList<Manga> lista) {
		this.usuario = userName;
		this.nome = nome;
		this.senha = senha;
		this.lista = lista;
	}

	public Usuario(String userName, String nome, ArrayList<Manga> lista) {
		this.usuario = userName;
		this.nome = nome;
		this.lista = lista;
	}

	public String getUsuario() {
		return this.usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public String getNome() {
		return this.nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getSenha() {
		return this.senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public ArrayList<Manga> getLista() {
		if (this.lista == null)
			return null;
		else
			return this.lista;
	}

	public void setLista(ArrayList<Manga> lista) {
		this.lista = lista;
	}

}
