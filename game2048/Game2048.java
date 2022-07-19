package com.javarush.games.game2048;

import com.javarush.engine.cell.Color;
import com.javarush.engine.cell.Game;
import com.javarush.engine.cell.Key;

public class Game2048 extends Game {
	private static final int SIDE = 4;
	private int[][] gameField = new int[SIDE][SIDE];
	private boolean isGameStopped = false;
	private int score = 0;

	private void win() {
		isGameStopped = true;
		showMessageDialog(Color.GRAY, "YOU WIN", Color.GOLD, 175);
	}


	private boolean mergeRow(int[] row) {
		boolean isChanged = false;

		for(int i = 0; i < row.length; i++) {
			for(int j = i + 1; j < row.length; j++) {
				if(row[i] != 0 && row[j] != 0
						&& j == i + 1 && row[i] == row[j]) {
					row[i] += row[j];
					score += row[i];
					setScore(score);
					row[j] = 0;
					i++;
					isChanged = true;
				}
			}
		}
		return isChanged;
	}

	private void moveLeft() {
		boolean isChanged = false;
		for(int[] row : gameField
		) {
			if(compressRow(row)) {
				isChanged = true;
			}
			if(mergeRow(row) && !isChanged) {
				isChanged = true;
			}
			if(compressRow(row) && !isChanged) {
				isChanged = true;
			}
		}
		if(isChanged) {
			createNewNumber();
		}
	}

	private void rotateClockwise() {

		int[][] row = new int[4][4];

		for(int i = 0; i < 4; i++) {
			for(int j = 0; j < 4; j++) {
				row[i][0] = gameField[3][i];
				row[i][1] = gameField[2][i];
				row[i][2] = gameField[1][i];
				row[i][3] = gameField[0][i];
				i++;
			}
		}

		for(int i = 0; i < 4; i++) {
			for(int j = 0; j < 4; j++) {
				gameField[i][j] = row[i][j];
			}
		}
	}

	private void moveRight() {

		rotateClockwise();
		rotateClockwise();
		moveLeft();
		rotateClockwise();
		rotateClockwise();

	}

	private void moveUp() {

		rotateClockwise();
		rotateClockwise();
		rotateClockwise();
		moveLeft();
		rotateClockwise();

	}

	private int getMaxTileValue() {
		int max = 0;
		for(int i = 0; i < SIDE; i++) {
			for(int j = 0; j < SIDE; j++) {
				if(gameField[i][j] >= max) {
					max = gameField[i][j];
				}
			}
		}
		return max;
	}

	private void moveDown() {

		rotateClockwise();
		moveLeft();
		rotateClockwise();
		rotateClockwise();
		rotateClockwise();
	}

	@Override
	public void onKeyPress(Key key) {
		if(!canUserMove()) {
			gameOver();
			return;
		}

		if(isGameStopped) {
			if(key == Key.SPACE) {
				isGameStopped = false;
				drawScene();
				createGame();
			}

		} else{
			switch(key) {

				case UP:
					moveUp();
					drawScene();
					break;
				case DOWN:
					moveDown();
					drawScene();
					break;
				case LEFT:
					moveLeft();
					drawScene();
					break;
				case RIGHT:
					moveRight();
					drawScene();
					break;
			}
	}

}

	private boolean compressRow(int[] row) {
		boolean isChanged = false;

		for(int i = 0; i < 4; i++) {
			for(int j = i; j < 4; j++) {
				if(row[i] == 0 && row[j] > 0) {
					row[i] = row[j];
					row[j] = 0;
					isChanged = true;
				}
			}
		}
		return isChanged;

	}

	private Color getColorByValue(int value) {
		Color color = null;
		switch(value) {
			case 0:
				color = Color.ANTIQUEWHITE;
				break;
			case 2:
				color = Color.AQUA;
				break;
			case 4:
				color = Color.ALICEBLUE;
				break;
			case 8:
				color = Color.BEIGE;
				break;
			case 16:
				color = Color.BISQUE;
				break;
			case 32:
				color = Color.CADETBLUE;
				break;
			case 64:
				color = Color.CORAL;
				break;
			case 128:
				color = Color.CYAN;
				break;
			case 256:
				color = Color.DARKGOLDENROD;
				break;
			case 512:
				color = Color.DARKCYAN;
				break;
			case 1024:
				color = Color.FIREBRICK;
				break;
			case 2048:
				color = Color.GOLD;
				break;
		}
		return color;
	}

	private void setCellColoredNumber(int x, int y, int value) {
		Color color = getColorByValue(value);
		if(value == 0) {
			setCellValueEx(x, y, color, "");
		} else {
			setCellValueEx(x, y, color, String.valueOf(value));
		}
	}

	private void gameOver() {
		isGameStopped = true;
		showMessageDialog(Color.GREY, "GAME OVER", Color.RED, 175);
	}

	private boolean canUserMove() {
		boolean isLegal = false;

		if(gameField[0][0] == gameField[1][0]
				| gameField[0][0] == gameField[0][1]
				| gameField[3][0] == gameField[2][0]
				| gameField[3][0] == gameField[3][1]
				| gameField[0][3] == gameField[0][2]
				| gameField[0][3] == gameField[1][3]
				| gameField[3][3] == gameField[3][2]
				| gameField[3][3] == gameField[2][3]
				| gameField[0][1] == gameField[0][2]
				| gameField[1][0] == gameField[2][0]
				| gameField[2][3] == gameField[1][3]
				| gameField[3][2] == gameField[3][1]) {
			isLegal = true;
		} else {
			for(int y = 0; y < SIDE; y++) {
				for(int x = 0; x < SIDE; x++) {

					if(gameField[y][x] == 0) {
						isLegal = true;
						break;
					} else if(y > 0 && y < 3 && x > 0 && x < 3) {
						if(gameField[y][x] == gameField[y + 1][x]
								| gameField[y][x] == gameField[y - 1][x]
								| gameField[y][x] == gameField[y][x + 1]
								| gameField[y][x] == gameField[y][x - 1]) {
							isLegal = true;
							break;
						}
					}
				}
			}
		}
		return isLegal;
	}

	private void createNewNumber() {
		if(getMaxTileValue() == 2048) {
			win();
			return;
		}
		int x = getRandomNumber(0, SIDE);
		int y = getRandomNumber(0, SIDE);
		if(gameField[x][y] != 0) {
			createNewNumber();
		} else {
			int value = getRandomNumber(10);
			if(value == 9 && gameField[x][y] == 0) {
				gameField[x][y] = 4;
			} else if(value < 9) {
				gameField[x][y] = 2;
			}
		}
	}

	private void drawScene() {

		for(int y = 0; y < SIDE; y++) {
			for(int x = 0; x < SIDE; x++) {
				setCellColoredNumber(x, y, gameField[y][x]);
			}
		}
	}

	private void createGame() {


		for(int y = 0; y < SIDE; y++) {
			for(int x = 0; x < SIDE; x++) {
				gameField[y][x] = 0;
			}
		}
		score = 0;
		setScore(score);


		createNewNumber();
		createNewNumber();

	}

	@Override
	public void initialize() {
		setScreenSize(SIDE, SIDE);
		createGame();
		drawScene();
	}
}
