package com.javarush.games.moonlander;

import com.javarush.engine.cell.Color;
import com.javarush.engine.cell.Game;
import com.javarush.engine.cell.Key;

public class MoonLanderGame extends Game {
	public static final int WIDTH = 64;
	public static final int HEIGHT = 64;
	private Rocket rocket;
	private GameObject landscape;
	private GameObject platform;
	private boolean isUpPressed;
	private boolean isLeftPressed;
	private boolean isRightPressed;
	private boolean isGameStopped;
	private int score;

	@Override
	public void initialize() {
		setScreenSize(WIDTH, HEIGHT);
		createGame();
		showGrid(false);
	}

	@Override
	public void onTurn(int step) {

		rocket.move(isUpPressed, isLeftPressed, isRightPressed);
		check();
		if(score > 0){
			score -= 1;
		}
		setScore(score);
		drawScene();
	}

	@Override
	public void setCellColor(int x, int y, Color color) {
		if(x > WIDTH - 1 || x < 0 || y < 0 || y > HEIGHT - 1) {
			return;
		}
		super.setCellColor(x, y, color);
	}

	@Override
	public void onKeyPress(Key key) {

		if(key == Key.SPACE & isGameStopped){
			createGame();
			return;
		}

		if(Key.RIGHT == key) {
			isRightPressed = true;
			isLeftPressed = false;
		} else if(Key.LEFT == key) {
			isLeftPressed = true;
			isRightPressed = false;
		} else if(Key.UP == key) {
			isUpPressed = true;
		}
	}

	@Override
	public void onKeyReleased(Key key) {
		if(Key.RIGHT == key) {
			isRightPressed = false;
		} else if(Key.LEFT == key) {
			isLeftPressed = false;
		} else if(Key.UP == key) {
			isUpPressed = false;
		}
	}

	private void createGame() {
		score = 1000;

		isGameStopped = false;
		setTurnTimer(50);
		createGameObjects();
		drawScene();
		isUpPressed = false;
		isLeftPressed = false;
		isRightPressed = false;
	}

	private void drawScene() {
		for(int y = 0; y < HEIGHT; y++) {
			for(int x = 0; x < WIDTH; x++) {
				setCellColor(x, y, Color.BLACK);
			}
		}

		rocket.draw(this);
		landscape.draw(this);
	}

	private void createGameObjects() {
		rocket = new Rocket(WIDTH / 2.0, 0);
		landscape = new GameObject(0, 25, ShapeMatrix.LANDSCAPE);
		platform = new GameObject(23, MoonLanderGame.HEIGHT - 1, ShapeMatrix.PLATFORM);

	}

	private void check() {
		if(rocket.isCollision(landscape) & !(rocket.isCollision(platform)
				& rocket.isStopped())) {
			gameOver();
		}

        if(rocket.isStopped() && rocket.isCollision(platform)){
            win();
        }
	}

	private void win() {
		rocket.land();
		isGameStopped = true;
		showMessageDialog(Color.GRAY, "YOU WIN", Color.GOLD, 100);
		stopTurnTimer();
	}

	private void gameOver() {

		rocket.crash();
		score = 0;
		
		isGameStopped = true;
		showMessageDialog(Color.GRAY, "GAME OVER", Color.RED, 100);
		stopTurnTimer();
	}
}
