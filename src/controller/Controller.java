package controller;

import javax.swing.JOptionPane;

import actions.FlipTileAction;
import actions.RightClickTileAction;
import model.Tile;
import model.TileState;
import view.View;

public class Controller implements ControllerInterface {

	private View view;
	private static final int safeZoneX = 4;
	private static final int safeZoneY = 2;
	private int maxMines;
	private int mineChance;
	private Tile[][] tiles;
	private int flagCount = 0;
	private Thread timer;

	public Controller() {
		this.view = new View();
		initTileStates();
	}

	private void initTileStates() {
		tiles = view.getTiles();

		for (Tile[] tmpTiles : tiles) {
			for (Tile tile : tmpTiles) {
				tile.addActionListener(new FlipTileAction((ControllerInterface) this));
				tile.addMouseListener(new RightClickTileAction((ControllerInterface) this));
			}
		}

		int tileCount = tiles[0].length * tiles.length;
		maxMines = tiles.length + tiles[0].length;
		mineChance = (int) (((float) maxMines / (float) tileCount) * 150);

		spawnMines();
		view.getTxtMines().setText("" + getMineCount());
	}

	@SuppressWarnings("unused")
	private void revealAll() {
		for (Tile[] tmpTiles : tiles) {
			for (Tile tile : tmpTiles) {
				tile.reveal();
			}
		}
	}

	@Override
	public void revealMines() {
		for (Tile[] tmpTiles : tiles) {
			for (Tile tile : tmpTiles) {
				if (TileState.MINE == tile.getState()) {
					tile.reveal();
				}
			}
		}
	}

	@Override
	public void checkWin() {
		if (flagCount == getMineCount()) {
			int correctFlags = 0;
			for (Tile[] tmpTiles : tiles) {
				for (Tile tile : tmpTiles) {
					if (TileState.MINE == tile.getState() && "F".equals(tile.getText())) {
						correctFlags++;
					}
				}
			}
			if (correctFlags == getMineCount()) {
				stopTimer();
				JOptionPane.showMessageDialog(view, "Winner");
			}
		}
	}

	private void spawnMines() {
		for (Tile[] tmpTiles : tiles) {
			for (Tile tile : tmpTiles) {
				int surroundedByMines = suroundedByMines(tile.getPosX(), tile.getPosY());
				if (surroundedByMines == 0 && rollMine() && rollMine()) {
					tile.setState(TileState.MINE);
				} else if (rollMine() && surroundedByMines < 5) {
					tile.setState(TileState.MINE);
				} else if (rollMine() && rollMine() && surroundedByMines < 7) {
					tile.setState(TileState.MINE);
				} else if (rollMine() && rollMine()) {
					tile.setState(TileState.MINE);
				}
			}
		}
	}

	@Override
	public void revealSurroundings(int x, int y) {
		if (tiles[x][y].getState() == TileState.EMPTY) {
			for (int i = -1; i <= 1; i++) {
				for (int j = -1; j <= 1; j++) {
					if (x - i >= 0 && x - i < tiles.length && y - j >= 0 && y - j < tiles[0].length) {
						Tile tile = tiles[x - i][y - j];
						if (!tile.isRevealed()) {
							tile.reveal();
							if (tile.getState() == TileState.EMPTY) {
								revealSurroundings(x - i, y - j);
							}
						}
					}
				}
			}
		}
	}

	private void spawnNumbers() {
		for (Tile[] tmpTiles : tiles) {
			for (Tile tile : tmpTiles) {
				if (tile.getState() != TileState.MINE) {
					int surroundedByMines = suroundedByMines(tile.getPosX(), tile.getPosY());
					if (surroundedByMines == 0) {
						tile.setState(TileState.EMPTY);
					} else if (surroundedByMines == 1) {
						tile.setState(TileState.ONE);
					} else if (surroundedByMines == 2) {
						tile.setState(TileState.TWO);
					} else if (surroundedByMines == 3) {
						tile.setState(TileState.THREE);
					} else if (surroundedByMines == 4) {
						tile.setState(TileState.FOUR);
					} else if (surroundedByMines == 5) {
						tile.setState(TileState.FIVE);
					} else if (surroundedByMines == 6) {
						tile.setState(TileState.SIX);
					}
				}
			}
		}
	}

	private int getMineCount() {
		int mineCount = 0;
		for (Tile[] tmpTiles : tiles) {
			for (Tile tile : tmpTiles) {
				if (tile.getState() == TileState.MINE) {
					mineCount++;
				}
			}
		}
		return mineCount;
	}

	private boolean rollMine() {
		int randomNumber = randomNumberBetween(0, 101);

		if (randomNumber > mineChance) {
			return false;
		} else {
			return true;
		}
	}

	private int randomNumberBetween(int min, int max) {
		return min + (int) (Math.random() * ((max - min) + 1));
	}

	private int suroundedByMines(int x, int y) {
		int surroundingMines = 0;
		for (int i = -1; i <= 1; i++) {
			for (int j = -1; j <= 1; j++) {
				if (x - i >= 0 && x - i < tiles.length && y - j >= 0 && y - j < tiles[0].length) {

					if (!(x - i == x && y - j == y)) {
						if (tiles[x - i][y - j].getState() == TileState.MINE) {
							surroundingMines++;
						}
					}
				}
			}
		}

		return surroundingMines;
	}

	@Override
	public void createSafeZone(int x, int y) {
		int lowEndX = safeZoneX / -2;
		int highEndX = safeZoneX / 2;
		int lowEndY = safeZoneY / -2;
		int highEndY = safeZoneY / 2;

		for (int i = lowEndX; i <= highEndX; i++) {
			for (int j = lowEndY; j <= highEndY; j++) {
				int tmpX = x + i;
				int tmpY = y + j;

				if (tmpX < 0) {
					highEndX++;
					i = lowEndX++;
					j = lowEndY;
					continue;
				} else if (tmpX >= tiles.length - 1) {
					highEndX--;
					i = lowEndX - (safeZoneX - (safeZoneX - highEndX));
					j = lowEndY;
					continue;
				}

				if (tmpY < 0) {
					highEndY++;
					i = lowEndX;
					j = lowEndY++;
					continue;
				} else if (tmpY >= tiles[0].length - 1) {
					highEndX--;
					j = lowEndY - (safeZoneY - (safeZoneY - highEndY));
					i = lowEndX;
					continue;
				}

				Tile tile = tiles[x + i][y + j];
				tile.setState(TileState.EMPTY);
			}
		}
		spawnNumbers();
		revealSurroundings(x, y);
	}

	@Override
	public void increaseFlagCount() {
		flagCount++;
		view.getTxtMines().setText("" + (getMineCount() - flagCount));
	}

	@Override
	public void reduceFlagCount() {
		flagCount--;
		view.getTxtMines().setText("" + (getMineCount() - flagCount));
	}

	@Override
	public void startTimer() {
		timer = new Thread(new Runnable() {

			int seconds = 0;

			@Override
			public void run() {

				while (timer.isInterrupted() == false) {
					int hours = seconds / (60 * 60);
					int minutes = (seconds / 60) - (hours * 60);
					int tmpSeconds = seconds - ((hours * 60 * 60) + (minutes * 60));
					String timeS = (hours < 10 ? "0" : "") + hours + ":" + (minutes < 10 ? "0" : "") + minutes + ":"
							+ (tmpSeconds < 10 ? "0" : "") + tmpSeconds;
					view.getLbTime().setText(timeS);
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					seconds++;
				}
			}
		});
		timer.start();

	}

	@SuppressWarnings("deprecation")
	@Override
	public void stopTimer() {
		timer.stop();

	}

	@Override
	public int getSurroundingFlagCount(Tile t) {

		int x = t.getPosX();
		int y = t.getPosY();

		int surroundingFlags = 0;
		for (int i = -1; i <= 1; i++) {
			for (int j = -1; j <= 1; j++) {
				if (x - i >= 0 && x - i < tiles.length && y - j >= 0 && y - j < tiles[0].length) {

					if (!(x - i == x && y - j == y)) {
						if (tiles[x - i][y - j].getText().equals("F")) {
							surroundingFlags++;
						}
					}
				}
			}
		}
		return surroundingFlags;
	}

	@Override
	public void doMiddleClick(Tile t) {
		int x = t.getPosX();
		int y = t.getPosY();

		for (int i = -1; i <= 1; i++) {
			for (int j = -1; j <= 1; j++) {
				if (x - i >= 0 && x - i < tiles.length && y - j >= 0 && y - j < tiles[0].length) {

					if (!(x - i == x && y - j == y)) {
						if (!tiles[x - i][y - j].isRevealed()) {
							tiles[x - i][y - j].doClick();
						}
					}
				}
			}
		}

	}

}
