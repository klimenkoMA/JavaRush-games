package com.javarush.games.spaceinvaders.gameobjects;

import com.javarush.engine.cell.Game;
import com.javarush.games.spaceinvaders.Direction;
import com.javarush.games.spaceinvaders.ShapeMatrix;
import com.javarush.games.spaceinvaders.SpaceInvadersGame;

import java.util.ArrayList;
import java.util.List;

public class EnemyFleet {

	private static final int ROWS_COUNT = 3;
	private static final int COLUMNS_COUNT = 10;
	private static final int STEP = ShapeMatrix.ENEMY.length + 1;
	private List<EnemyShip> ships;
	private Direction direction = Direction.RIGHT;

	public EnemyFleet() {
		createShips();
	}

	public int verifyHit(List<Bullet> bullets) {

		if(bullets.isEmpty()) {
			return 0;
		}
		int summa = 0;

		for(EnemyShip ship : ships
		) {
			for(Bullet bullet : bullets) {
				if(ship.isCollision(bullet) & ship.isAlive
						& bullet.isAlive) {
					ship.kill();
					bullet.kill();
					summa += ship.score;
				}

			}
		}
		return summa;
	}

	public double getBottomBorder() {
		double max = Double.MIN_VALUE;

		for(EnemyShip ship : ships
		) {
			if(ship.y + ship.height >= max) {
				max = ship.y + ship.height;
			}
		}
		return max;
	}

	public int getShipsCount() {
		return ships.size();
	}

	public void deleteHiddenShips() {

		ships.removeIf(enemyShip -> !enemyShip.isVisible());
	}

	public void move() {

		if(ships.isEmpty()) {
			return;
		}
		boolean isChanged = false;


		switch(direction) {
			case LEFT:
				if(getLeftBorder() < 0) {
					direction = Direction.RIGHT;
					isChanged = true;
				}
				break;
			case RIGHT:
				if(getRightBorder() > SpaceInvadersGame.WIDTH) {
					direction = Direction.LEFT;
					isChanged = true;
				}
				break;
		}

		double speed = getSpeed();
		if(isChanged) {
			for(EnemyShip ship : ships
			) {
				ship.move(Direction.DOWN, speed);
			}
		} else {
			for(EnemyShip ship : ships
			) {
				ship.move(direction, speed);
			}
		}

	}

	public Bullet fire(Game game) {
		if(ships.isEmpty()) {
			return null;
		}
		int fireStatus = game.getRandomNumber(100 / SpaceInvadersGame.COMPLEXITY);
		if(fireStatus > 0) {
			return null;
		}
		int index = game.getRandomNumber(ships.size());
		return ships.get(index).fire();
	}

	private double getSpeed() {
		return Math.min(2.0, 3.0 / ships.size());
	}

	private double getLeftBorder() {
		double min = Double.MAX_VALUE;

		for(EnemyShip ship : ships) {
			if(ship.x <= min) {
				min = ship.x;
			}
		}
		return min;
	}

	private double getRightBorder() {

		double max = Double.MIN_VALUE;

		for(EnemyShip ship : ships) {
			if(ship.x + ship.width >= max) {
				max = ship.x + ship.width;
			}
		}
		return max;
	}

	public void draw(Game game) {
		for(EnemyShip ship : ships
		) {
			ship.draw(game);
		}
	}

	private void createShips() {
		ships = new ArrayList<>();
		ships.add(new Boss(STEP * COLUMNS_COUNT / 2
				- ShapeMatrix.KILL_BOSS_ANIMATION_FIRST.length / 2 - 1, 5));

		for(int y = 0; y < ROWS_COUNT; y++) {
			for(int x = 0; x < COLUMNS_COUNT; x++) {
				ships.add(new EnemyShip(x * STEP, y * STEP + 12));
			}
		}
	}
}
