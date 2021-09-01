package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class Manga implements Serializable {
	private static final long serialVersionUID = 1L;

	String titulo;
	String descricao;
	String lista;
	ArrayList<Integer> listaVolumes = new ArrayList<Integer>();

	public Manga(String titulo, String descricao) {
		this.titulo = titulo;
		this.descricao = descricao;
	}

	public void insereVolume(int volume) throws Exception {

			if (listaVolumes.contains(volume)) {
				throw new Exception();
			} else {
				listaVolumes.add(volume);
				Collections.sort(listaVolumes);
			}

	}

	public void removeVolume(int volume) throws Exception {

			if (listaVolumes.contains(volume)) {
				listaVolumes.remove(Integer.valueOf(volume));
				Collections.sort(listaVolumes);
			} else {
				throw new Exception();
			}

	}

	public String getTitulo() {
		return titulo;
	}
	
	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public void setListaVolumes (ArrayList<Integer> listaVolumes) {
		this.listaVolumes = listaVolumes;
		
	}
	public void ConverterListaVolumes (String listaVolumes) {
		@SuppressWarnings("resource")
		Scanner scanner = new Scanner(listaVolumes);
		ArrayList<Integer> list = new ArrayList<Integer>();
		while (scanner.hasNextInt()) {
		    list.add(scanner.nextInt());
		}
		this.listaVolumes = list;		
	}
	
	public void DesconverterListaVolumes() {
	       StringBuilder str = new StringBuilder();
	       for (int i = 0; i < this.listaVolumes.size(); i++) 
	       {
	           int myNumbersInt = this.listaVolumes.get(i);
	           str.append("  " +myNumbersInt);
	       }
	      this.lista = str.toString();
	}
	
	public void setListaVolumes (String listaVolumes) {
		this.lista = listaVolumes;
		
	}
	
	public ArrayList<Integer> getListaVolumes() {
		if (listaVolumes == null) {
			return null;
		} else
		return listaVolumes;
	}
	public Object getListaVolumes2() {
		if (lista == null) {
			return null;
		} else
		return lista;
	}


}
