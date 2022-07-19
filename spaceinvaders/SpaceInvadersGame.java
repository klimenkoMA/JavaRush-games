package com.javarush.games.spaceinvaders;

import com.javarush.engine.cell.*;
import com.javarush.games.spaceinvaders.gameobjects.Bullet;
import com.javarush.games.spaceinvaders.gameobjects.EnemyFleet;
import com.javarush.games.spaceinvaders.gameobjects.PlayerShip;
import com.javarush.games.spaceinvaders.gameobjects.Star;

import java.util.ArrayList;
import java.util.List;

public class SpaceInvadersGame extends Game {

	public static final int WIDTH = 64;
	public static final int HEIGHT = 64;
	public static final int COMPLEXITY = 5;
	private List<Star> stars;
	private EnemyFleet enemyFleet;
	private List<Bullet> enemyBullets;
	private PlayerShip playerShip;
	private boolean isGameStopped;
	private int animationsCount;
	private List<Bullet> playerBullets;
	private static final int PLAYER_BULLETS_MAX = 1;
	private int score;

	private void createStars(){
		stars = new ArrayList<>();
		stars.add(new Star(8,10));
		stars.add(new Star(14,62));
		stars.add(new Star(58,15));
		stars.add(new Star(47,47));
		stars.add(new Star(55,19));
		stars.add(new Star(22,53));
		stars.add(new Star(8,8));
		stars.add(new Star(18,37));
	}

	@Override
	public void onKeyPress(Key key) {
		switch(key){
			case SPACE :
				if(isGameStopped){
					createGame();
					break;
				}
				Bullet bullet = playerShip.fire();
				if(bullet != null & playerBullets.size() < PLAYER_BULLETS_MAX){
					playerBullets.add(bullet);
				}

				break;
			case LEFT:
				playerShip.setDirection(Direction.LEFT);
				break;
			case RIGHT:
				playerShip.setDirection(Direction.RIGHT);
				break;
		}
	}

	@Override
	public void onKeyReleased(Key key) {
		switch(key){
			case LEFT :
				if(playerShip.getDirection() == Direction.LEFT){
					playerShip.setDirection(Direction.UP);
				}
				break;
			case RIGHT:
				if(playerShip.getDirection() == Direction.RIGHT){
					playerShip.setDirection(Direction.UP);
				}
				break;
		}
	}

	private void moveSpaceObjects(){
		enemyFleet.move();
		for(Bullet bullet: enemyBullets
		    ) {
			bullet.move();
		}
		for(Bullet bullet: playerBullets
		) {
			bullet.move();
		}
		playerShip.move();
	}

	private void removeDeadBullets(){
		enemyBullets.removeIf(bullet -> bullet.y >= HEIGHT - 1);
		enemyBullets.removeIf(bullet -> !bullet.isAlive);
		playerBullets.removeIf(bullet -> !bullet.isAlive);
		playerBullets.removeIf(bullet -> bullet.y + bullet.height < 0);
	}

	@Override
	public void setCellValueEx(int x, int y, Color cellColor, String value) {
		if(x < 0 || x >= WIDTH || y < 0 || y >= HEIGHT ){
			return;
		}

		super.setCellValueEx(x, y, cellColor, value);
	}

	private void check(){
		if(!playerShip.isAlive){
			stopGameWithDelay();
		}
		playerShip.verifyHit(enemyBullets);
		score += enemyFleet.verifyHit(playerBullets);
		enemyFleet.deleteHiddenShips();
		removeDeadBullets();

		double border = enemyFleet.getBottomBorder();
		if(border >= playerShip.y){
			playerShip.kill();
		}

		int count = enemyFleet.getShipsCount();
		if(count == 0){
			playerShip.win();
			stopGameWithDelay();
		}

	}

	@Override
	public void initialize() {
		setScreenSize(WIDTH, HEIGHT);
		createGame();
	}

	@Override
	public void onTurn(int step) {
		check();
		Bullet bullet = enemyFleet.fire(this);
		if(bullet != null){
			enemyBullets.add(bullet);
		}
		setScore(score);
		moveSpaceObjects();
		drawScene();
	}

	private void  stopGame(boolean isWin){
		isGameStopped = true;
		stopTurnTimer();

		if(isWin){
			showMessageDialog(Color.WHITE, "You win"
			, Color.GREEN, 75);
		}else {
			showMessageDialog(Color.WHITE, "You lose"
					, Color.RED, 75);
		}
	}

	private void stopGameWithDelay(){
		animationsCount ++;
		if(animationsCount >= 10) {
			stopGame(playerShip.isAlive);
		}
	}

	private void createGame(){
		setTurnTimer(40);
		score = 0;
		playerBullets = new ArrayList<>();
		playerShip = new PlayerShip();
		enemyFleet = new EnemyFleet();
		enemyBullets = new ArrayList<>();
		isGameStopped = false;
		animationsCount = 0;
		createStars();
		drawScene();
	}
	private void drawScene(){
		drawField();
		enemyFleet.draw(this);
		playerShip.draw(this);
		for(Bullet bullet: enemyBullets
		    ) {
			bullet.draw(this);
		}
		for(Bullet bullet: playerBullets
		) {
			bullet.draw(this);
		}

	}
	private void drawField(){
		for(int y = 0; y < HEIGHT; y++) {
			for(int x = 0; x < WIDTH; x++) {
				setCellValueEx(x, y, Color.BLACK, "");
			}
		}
		for(Star star: stars
		) {
			star.draw(this);
		}
	}
}
