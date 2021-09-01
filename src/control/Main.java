package control;

import java.io.IOException;
import java.sql.*;
import java.awt.EventQueue;
import view.Janela_Inicial;

/*
 * PROGRAMA FEITO POR LUCAS MALHEIROS 1835793
 * */

public class Main {

	public static void main(String[] args) throws IOException, ClassNotFoundException, SQLException {

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

}
