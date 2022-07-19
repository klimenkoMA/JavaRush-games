package com.javarush.games.minesweeper;

import com.javarush.engine.cell.Color;
import com.javarush.engine.cell.Game;

import java.util.ArrayList;
import java.util.List;

public class MinesweeperGame extends Game {
	private static final int SIDE = 9;
	private static final String MINE = "\uD83D\uDCA3";
	private static final String FLAG = "\uD83D\uDEA9";
	private GameObject[][] gameField = new GameObject[SIDE][SIDE];
	private int countFlags;
	private int countMinesOnField;
	private boolean isGameStopped;
	private int countClosedTiles = SIDE * SIDE;
	private int score;


	@Override
	public void onMouseRightClick(int x, int y) {
		markTile(x, y);
	}

	private void markTile(int x, int y){
		if(!isGameStopped) {
			GameObject gameObject = gameField[y][x];
			if(!gameObject.isOpen && countFlags != 0) {
				if(!gameObject.isFlag) {
					gameObject.isFlag = true;
					countFlags--;
					setCellValue(x, y, FLAG);
					setCellColor(x, y, Color.YELLOW);
				} else {
					gameObject.isFlag = false;
					countFlags++;
					setCellValue(x, y, "");
					setCellColor(x, y, Color.ORANGE);
				}
			}
		}
	}

	private void win(){
		isGameStopped = true;
		showMessageDialog(Color.GREY, "YOU WIN!", Color.LIGHTGOLDENRODYELLOW, 175);
	}

	private void restart(){
		isGameStopped = false;
		countClosedTiles = SIDE * SIDE;
		score = 0;
		countMinesOnField = 0;
		setScore(score);

		createGame();

	}

	private void openTile(int x, int y) {
		GameObject gameObject = gameField[y][x];
		if(!gameObject.isOpen && !gameObject.isFlag && !isGameStopped) {
			gameObject.isOpen = true;

			countClosedTiles --;
			setCellColor(x, y, Color.GREEN);
			if(gameObject.isMine) {
				setCellValueEx(gameObject.x, gameObject.y, Color.RED, MINE);
				gameOver();
			} else if(gameObject.countMineNeighbors == 0) {

				setCellValue(gameObject.x, gameObject.y, "");
				List<GameObject> neighbors = getNeighbors(gameObject);
				for(GameObject obj : neighbors
				) {
					if(!obj.isOpen) {
						openTile(obj.x, obj.y);
					} else if(!obj.isMine && obj.countMineNeighbors != 0) {
						setCellNumber(x, y, gameObject.countMineNeighbors);
					}
				}
			} else {
				setCellNumber(x, y, gameObject.countMineNeighbors);

				score += 5;
				setScore(score);
			}

			if(countClosedTiles == countMinesOnField && !gameObject.isMine){
				win();
			}
		}
	}

	@Override
	public void onMouseLeftClick(int x, int y) {
		if(isGameStopped){
			restart();
		}else {
			openTile(x, y);
		}
	}

	private void countMineNeighbors() {
		for(int y = 0; y < SIDE; y++) {
			for(int x = 0; x < SIDE; x++) {
				GameObject obj = gameField[y][x];
				if(!obj.isMine) {
					List<GameObject> neighbors = getNeighbors(obj);
					int countMines = 0;
					for(GameObject object : neighbors
					) {
						if(object.isMine) {
							countMines++;
						}
					}
					obj.countMineNeighbors = countMines;
				}
			}
		}
	}

	@Override
	public void initialize() {
		setScreenSize(SIDE, SIDE);
		createGame();
	}

	private void gameOver(){
		isGameStopped = true;
		showMessageDialog(Color.GREY, "GAME OVER", Color.RED, 175);
	}

	private void createGame() {

		for(int y = 0; y < SIDE; y++) {
			for(int x = 0; x < SIDE; x++) {
				
				setCellValue(x, y, "");
			}
		}

		for(int y = 0; y < SIDE; y++) {
			for(int x = 0; x < SIDE; x++) {
				boolean isMine = getRandomNumber(10) < 1;
				if(isMine) {
					countMinesOnField++;
				}
				gameField[y][x] = new GameObject(x, y, isMine);
				setCellColor(x, y, Color.ORANGE);
			}
		}
		countMineNeighbors();
		countFlags = countMinesOnField;
	}

	private List<GameObject> getNeighbors(GameObject gameObject) {
		List<GameObject> result = new ArrayList<>();
		for(int y = gameObject.y - 1; y <= gameObject.y + 1; y++) {
			for(int x = gameObject.x - 1; x <= gameObject.x + 1; x++) {
				if(y < 0 || y >= SIDE) {
					continue;
				}
				if(x < 0 || x >= SIDE) {
					continue;
				}
				if(gameField[y][x] == gameObject) {
					continue;
				}
				result.add(gameField[y][x]);
			}
		}
		return result;
	}
}