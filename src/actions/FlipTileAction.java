package actions;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import controller.ControllerInterface;
import model.Tile;
import model.TileState;

public class FlipTileAction implements ActionListener {

	private static boolean firstClick = true;
	private static boolean gameOver = false;
	private ControllerInterface controllerInterface;

	public FlipTileAction(ControllerInterface controllerInterface) {
		this.controllerInterface = controllerInterface;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Tile tile = (Tile) e.getSource();
		checkTile(tile);
	}

	private void checkTile(Tile tile) {
		if (!gameOver) {
			if (firstClick) {
				firstClick = false;
				tile.setState(TileState.EMPTY);
				controllerInterface.createSafeZone(tile.getPosX(), tile.getPosY());
				controllerInterface.startTimer();
			} else if (!"F".equals(tile.getText())) {
				tile.reveal();
				if (tile.getState() == TileState.EMPTY) {
					controllerInterface.revealSurroundings(tile.getPosX(), tile.getPosY());
				} else if (tile.getState() == TileState.MINE) {
					gameOver = true;
					new Thread(new Runnable() {

						@Override
						public void run() {
							controllerInterface.revealMines();
						}
					}).start();
					controllerInterface.stopTimer();
					JOptionPane.showMessageDialog(tile, "Game Over");
				}
			}
		}
		controllerInterface.checkWin();
	}
}
