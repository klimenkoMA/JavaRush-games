package com.javarush.games.spaceinvaders.gameobjects;

import com.javarush.games.spaceinvaders.Direction;
import com.javarush.games.spaceinvaders.ShapeMatrix;
import com.javarush.games.spaceinvaders.SpaceInvadersGame;

import java.util.List;

public class PlayerShip extends Ship {

	private Direction direction = Direction.UP;

	public PlayerShip() {
		super(SpaceInvadersGame.WIDTH / 2.0,
				SpaceInvadersGame.HEIGHT - ShapeMatrix.PLAYER.length - 1);
		setStaticView(ShapeMatrix.PLAYER);
	}
	public void win(){
		setStaticView(ShapeMatrix.WIN_PLAYER);
	}

	@Override
	public Bullet fire() {
		if(!this.isAlive){
			return null;
		}

		return new Bullet(x + 2, y - ShapeMatrix.BULLET.length, Direction.UP);
	}

	public Direction getDirection() {
		return direction;
	}

	public void setDirection(Direction newDirection) {
		if(newDirection != Direction.DOWN) {
			direction = newDirection;
		}
	}

	public void move() {

		if(!isAlive) {
			return;
		}

		switch(direction) {
			case LEFT:
				x --;
				break;
			case RIGHT:
				x ++;
				break;
		}

		if(x < 0){
			x = 0;
		}

		if((x + width) > SpaceInvadersGame.WIDTH){
			x = SpaceInvadersGame.WIDTH - width;
		}
	}

	@Override
	public void kill() {

		if(!this.isAlive) {
			return;
		}
		isAlive = false;
		super.setAnimatedView(false ,ShapeMatrix.KILL_PLAYER_ANIMATION_FIRST
				, ShapeMatrix.KILL_PLAYER_ANIMATION_SECOND
				, ShapeMatrix.KILL_PLAYER_ANIMATION_THIRD
				, ShapeMatrix.DEAD_PLAYER);
	}

	public void verifyHit(List<Bullet> bullets) {
		if(bullets.isEmpty()) {
			return;
		}

		for(Bullet bullet : bullets
		) {
			if(this.isAlive & bullet.isAlive) {
				if(this.isCollision(bullet)) {
					kill();
					bullet.kill();
				}
			}
		}
	}
}
