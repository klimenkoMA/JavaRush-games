package com.javarush.games.spaceinvaders.gameobjects;

import com.javarush.games.spaceinvaders.Direction;
import com.javarush.games.spaceinvaders.ShapeMatrix;

public class EnemyShip extends Ship {

	public int score = 15;

	public EnemyShip(double x, double y) {
		super(x, y);
		setStaticView(ShapeMatrix.ENEMY);
	}

	@Override
	public Bullet fire() {
		return new Bullet(x + 1, y + height, Direction.DOWN);
	}

	@Override
	public void kill() {
		if(!this.isAlive) {
			return;
		}
		isAlive = false;
		super.setAnimatedView(false, ShapeMatrix.KILL_ENEMY_ANIMATION_FIRST
				, ShapeMatrix.KILL_ENEMY_ANIMATION_SECOND
				, ShapeMatrix.KILL_ENEMY_ANIMATION_THIRD);
	}

	public void move(Direction direction, double speed) {
		switch(direction) {
			case RIGHT:
				x += speed;
				break;
			case LEFT:
				x -= speed;
				break;
			case DOWN:
				y += 2;
				break;
		}
	}
}
