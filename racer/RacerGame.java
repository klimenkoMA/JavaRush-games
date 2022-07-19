package com.javarush.games.racer;

import com.javarush.engine.cell.Color;
import com.javarush.engine.cell.Game;
import com.javarush.engine.cell.Key;
import com.javarush.games.racer.road.RoadManager;

public class RacerGame extends Game {
	public static final int WIDTH = 64;
	public static final int HEIGHT = 64;
	public static final int CENTER_X = WIDTH / 2;
	public static final int ROADSIDE_WIDTH = 14;
	private static final int RACE_GOAL_CARS_COUNT = 40;
	private RoadMarking roadMarking;
	private PlayerCar player;
	private RoadManager roadManager;
	private boolean isGameStopped;
	private FinishLine finishLine;
	private ProgressBar progressBar;
	private int score;


	@Override
	public void setCellColor(int x, int y, Color color) {
		if(x < 0 || x > WIDTH - 1 || y < 0 || y > HEIGHT - 1) {
			return;
		}
		super.setCellColor(x, y, color);
	}

	@Override
	public void initialize() {
		showGrid(false);
		setScreenSize(WIDTH, HEIGHT);
		createGame();
	}

	private void gameOver() {
		isGameStopped = true;
		showMessageDialog(Color.GRAY, "GAME OVER", Color.RED, 100);
		stopTurnTimer();
		player.stop();
	}

	private void
	createGame() {
		isGameStopped = false;
		score = 3500;
		setTurnTimer(40);
		player = new PlayerCar();
		roadMarking = new RoadMarking();
		roadManager = new RoadManager();
		finishLine = new FinishLine();
		progressBar = new ProgressBar(RACE_GOAL_CARS_COUNT);
		drawScene();
	}

	private void drawScene() {
		drawField();
		roadMarking.draw(this);
		player.draw(this);
		roadManager.draw(this);
		finishLine.draw(this);
		progressBar.draw(this);
	}

	@Override
	public void onTurn(int step) {


		if(!roadManager.checkCrush(player)) {

			roadManager.generateNewRoadObjects(this);


			if(roadManager.getPassedCarsCount() >= RACE_GOAL_CARS_COUNT) {
				finishLine.show();
			}
			if(finishLine.isCrossed(player)) {
				win();
				drawScene();
				return;
			}

			moveAll();
			score -= 5;
			setScore(score);
			drawScene();

		} else {
			gameOver();
			drawScene();
		}
	}

	@Override
	public void onKeyReleased(Key key) {
		switch(key) {
			case RIGHT:
				if(player.getDirection() == Direction.RIGHT) {
					player.setDirection(Direction.NONE);
				}
				break;
			case LEFT:
				if(player.getDirection() == Direction.LEFT) {
					player.setDirection(Direction.NONE);
				}
				break;
			case UP:
				player.speed = 1;
				break;
		}
	}

	@Override
	public void onKeyPress
			(Key
					 key) {
		switch(key) {
			case SPACE:
				if(isGameStopped) {
					createGame();
					return;
				}
				break;
			case RIGHT:
				player.setDirection(Direction.RIGHT);
				break;
			case LEFT:
				player.setDirection(Direction.LEFT);
				break;
			case UP:
				player.speed = 2;
				break;
		}
	}

	private void win() {
		isGameStopped = true;
		showMessageDialog(Color.GRAY, "YOU WIN", Color.GOLD, 100);
		stopTurnTimer();
	}

	private void moveAll
			() {
		roadMarking.move(player.speed);
		roadManager.move(player.speed);
		finishLine.move(player.speed);
		progressBar.move(roadManager.getPassedCarsCount());
		player.move();
	}

	private void drawField
			() {
		for(int i = 0; i < WIDTH; i++) {
			for(int j = 0; j < HEIGHT; j++) {
				if(j == CENTER_X) {
					setCellColor(j, i, Color.WHITE);
				} else if(j >= ROADSIDE_WIDTH && j < WIDTH - ROADSIDE_WIDTH) {
					setCellColor(j, i, Color.DIMGRAY);
				} else
					setCellColor(j, i, Color.GREEN);
			}
		}
	}

}
