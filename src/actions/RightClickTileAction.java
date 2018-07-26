package actions;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.SwingUtilities;

import controller.ControllerInterface;
import model.Tile;

public class RightClickTileAction extends MouseAdapter {

	private Tile tile;
	private ControllerInterface controllerInterface;

	public RightClickTileAction(ControllerInterface controllerInterface) {
		this.controllerInterface = controllerInterface;
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if (SwingUtilities.isRightMouseButton(e)) {
			tile = (Tile) e.getSource();
			rightClick();
		} else if (SwingUtilities.isMiddleMouseButton(e)) {
			tile = (Tile) e.getSource();
			middleClick();
		}

	}

	private void rightClick() {
		if (!tile.isRevealed()) {
			if ("".equals(tile.getText())) {
				tile.setText("F");
				controllerInterface.increaseFlagCount();
				controllerInterface.checkWin();
			} else if ("F".equals(tile.getText())) {
				tile.setText("?");
				controllerInterface.reduceFlagCount();
			} else if ("?".equals(tile.getText())) {
				tile.setText("");
			}
		}
	}

	private void middleClick() {
		if (tile.isRevealed()) {
			if (controllerInterface.getSurroundingFlagCount(tile) == tile.getStateAsInt()) {
				controllerInterface.doMiddleClick(tile);
			}
		}
	}

}
