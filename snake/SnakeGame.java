package com.javarush.games.snake;

import com.javarush.engine.cell.Color;
import com.javarush.engine.cell.Game;
import com.javarush.engine.cell.Key;

public class SnakeGame extends Game {
	public static final int WIDTH = 15;
	public static final int HEIGHT = 15;
	private static final int GOAL = 28;
	private Snake snake;
	private int turnDelay;
	private Apple apple;
	private boolean isGameStopped;
	private int score;

	private void createNewApple(){

		int width = getRandomNumber(WIDTH);
		int height = getRandomNumber(HEIGHT);
		apple = new Apple(width, height);

		if(snake.checkCollision(apple)){
			createNewApple();
		}
	}

	private void win(){

		stopTurnTimer();
		isGameStopped = true;
		showMessageDialog(Color.WHEAT, "YOU WIN", Color.GOLD, 175);
	}

	@Override
	public void onTurn(int step) {
		if(!apple.isAlive){
			createNewApple();
			score += 5;
			setScore(score);
			turnDelay -=10;
			setTurnTimer(turnDelay);
		}

		if(!snake.isAlive){
			gameOver();
		}

		if(snake.getLength() > GOAL){
			win();
		}
		
		snake.move(apple);
		drawScene();
	}

	private void gameOver(){

		stopTurnTimer();
		isGameStopped = true;
		showMessageDialog(Color.GREY, "Game over", Color.RED, 175);
	}

	private void createGame() {
		score = 0;
		setScore(score);
		snake = new Snake(WIDTH / 2, HEIGHT / 2);
		createNewApple();
		turnDelay = 300;
		setTurnTimer(turnDelay);
		isGameStopped = false;
		drawScene();
	}

	@Override
	public void onKeyPress(Key key) {

		switch(key) {
			case UP:
				snake.setDirection(Direction.UP);
				break;
			case DOWN:
				snake.setDirection(Direction.DOWN);
				break;
			case LEFT:
				snake.setDirection(Direction.LEFT);
				break;
			case RIGHT:
				snake.setDirection(Direction.RIGHT);
				break;
			case SPACE: if(isGameStopped){
				createGame();
			}
		}
	}

	private void drawScene() {
		for(int i = 0; i < HEIGHT; i++) {
			for(int j = 0; j < WIDTH; j++) {

				setCellValueEx(i, j, Color.GRAY, "");
			}
		}
		snake.draw(this);
		apple.draw(this);
	}

	@Override
	public void initialize() {
		setScreenSize(WIDTH, HEIGHT);
		createGame();
	}
}
