package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;

import model.Tile;

public class View extends JFrame {

	private Tile[][] tiles;
	private Dimension tileDim = new Dimension(30, 30);
	private JTextField txtMines = new JTextField();
	private JLabel lbTime = new JLabel("00:00:00");
	private static final Font font = new Font("Arial Bold", 1, 25);

	public View() {
		init();
	}

	private void init() {
		int x, y;
		x = Integer.parseInt(JOptionPane.showInputDialog("X"));
		y = Integer.parseInt(JOptionPane.showInputDialog("Y"));
		tiles = new Tile[x][y];

		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.add(getBoard(x, y), BorderLayout.CENTER);
		this.add(getStats(), BorderLayout.SOUTH);
		this.pack();
		this.validate();
		this.setVisible(true);

	}

	public JPanel getBoard(int x, int y) {
		JPanel panel = new JPanel(new GridLayout(y, x));
		for (int i = 0; i < y; i++) {
			for (int j = 0; j < x; j++) {
				Tile tmp = new Tile(j, i);
				tmp.setBorder(new LineBorder(Color.black, 1));
				tmp.setPreferredSize(tileDim);
				tiles[j][i] = tmp;
				panel.add(tmp);
			}
		}

		return panel;
	}

	public JPanel getStats() {
		JPanel panel = new JPanel(new BorderLayout());

		panel.add(getTime(), BorderLayout.CENTER);
		panel.add(getAssumptionOfMinesLeft(), BorderLayout.EAST);

		return panel;
	}

	public JPanel getTime() {
		JPanel panel = new JPanel(new BorderLayout());
		lbTime.setFont(font);
		panel.add(lbTime);

		return panel;
	}

	public JPanel getAssumptionOfMinesLeft() {
		JPanel panel = new JPanel(new BorderLayout());
		JLabel lbMines = new JLabel("Mines:");
		lbMines.setFont(font);
		panel.add(lbMines, BorderLayout.WEST);

		txtMines.setPreferredSize(new Dimension(50, 50));
		txtMines.setEditable(false);
		txtMines.setFont(font);
		panel.add(txtMines, BorderLayout.EAST);

		return panel;
	}

	public Tile[][] getTiles() {
		return tiles;
	}

	public JTextField getTxtMines() {
		return txtMines;
	}

	public JLabel getLbTime() {
		return lbTime;
	}
}
