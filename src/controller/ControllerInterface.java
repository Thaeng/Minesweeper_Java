package controller;

import model.Tile;

public interface ControllerInterface {

	public void revealSurroundings(int x, int y);

	public void createSafeZone(int x, int y);

	public void revealMines();

	public int getSurroundingFlagCount(Tile t);

	public void increaseFlagCount();

	public void reduceFlagCount();

	public void checkWin();

	public void startTimer();

	public void stopTimer();

	public void doMiddleClick(Tile t);
}
