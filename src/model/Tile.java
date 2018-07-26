package model;

import java.awt.Color;

import javax.swing.JButton;
import javax.swing.border.LineBorder;

@SuppressWarnings("serial")
public class Tile extends JButton {

	private TileState state;
	private boolean revealed = false;

	int posX;
	int posY;

	private static final Color C_UNREVEALED = Color.gray;
	private static final Color C_REVEALED = Color.lightGray;

	public Tile(int x, int y) {
		this(TileState.UNREVEALED, x, y);
	}

	public Tile(TileState state, int x, int y) {
		this.state = state;
		this.posX = x;
		this.posY = y;
		updateTile();
	}

	public TileState getState() {
		return this.state;
	}

	public void setState(TileState state) {
		this.state = state;
	}

	public boolean isRevealed() {
		return this.revealed;
	}

	public void reveal() {
		if (!revealed) {
			switch (state) {
			case EMPTY:
				this.setText("");
				break;
			case ONE:
				this.setText("1");
				break;
			case TWO:
				this.setText("2");
				break;
			case THREE:
				this.setText("3");
				break;
			case FOUR:
				this.setText("4");
				break;
			case FIVE:
				this.setText("5");
				break;
			case SIX:
				this.setText("6");
				break;
			case MINE:
				if ("F".equals(this.getText())) {
					this.setBorder(new LineBorder(Color.GREEN, 2));
				}
				this.setText("M");
				break;

			default:
				break;
			}
			this.revealed = true;
			updateTile();
		}
	}

	private void updateTile() {
		switch (state) {
		case UNREVEALED:
			this.setBackground(C_UNREVEALED);
			break;
		default:
			this.setBackground(C_REVEALED);
			break;
		}
	}

	public int getPosX() {
		return posX;
	}

	public int getPosY() {
		return posY;
	}

	public void setRevealed(boolean revealed) {
		this.revealed = revealed;
	}

	public int getStateAsInt() {
		switch (state) {
		case EMPTY:
			return 0;
		case ONE:
			return 1;
		case TWO:
			return 2;
		case THREE:
			return 3;
		case FOUR:
			return 4;
		case FIVE:
			return 5;
		case SIX:
			return 6;
		case MINE:
			return 7;

		default:
			return 0;
		}
	}

}
