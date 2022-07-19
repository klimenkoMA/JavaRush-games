package com.javarush.games.snake;

import com.javarush.engine.cell.Color;
import com.javarush.engine.cell.Game;

import java.util.ArrayList;
import java.util.List;

public class Snake {
	private static final String HEAD_SIGN = "\uD83D\uDC7E";
	private static final String BODY_SIGN = "\u26AB";
	public boolean isAlive = true;
	private Direction direction = Direction.LEFT;
	private List<GameObject> snakeParts = new ArrayList<>();

	public Snake(int x, int y) {
		snakeParts.add(new GameObject(x, y));
		snakeParts.add(new GameObject(x + 1, y));
		snakeParts.add(new GameObject(x + 2, y));
	}

	public int getLength() {
		return snakeParts.size();
	}

	public boolean checkCollision(GameObject gameObject) {
		boolean isTouch = false;
		for(GameObject obj : snakeParts
		) {
			if(obj.x == gameObject.x && obj.y == gameObject.y) {
				isTouch = true;
				break;
			}
		}
		return isTouch;
	}

	public void removeTail() {
		snakeParts.remove(snakeParts.size() - 1);
	}

	public GameObject createNewHead() {
		GameObject gameObject = null;
		GameObject head = snakeParts.get(0);
		switch(direction) {
			case UP:
				gameObject = new GameObject(head.x, head.y - 1);
				break;
			case DOWN:
				gameObject = new GameObject(head.x, head.y + 1);
				break;
			case LEFT:
				gameObject = new GameObject(head.x - 1, head.y);
				break;
			case RIGHT:
				gameObject = new GameObject(head.x + 1, head.y);
				break;
		}
		return gameObject;
	}


	public void move(Apple apple) {

		GameObject head = createNewHead();

		if(head.x < 0 || head.x >= SnakeGame.WIDTH
				|| head.y < 0 || head.y >= SnakeGame.HEIGHT) {
			isAlive = false;
			return;
		}
		if(checkCollision(head)) {
			isAlive = false;
			return;
		}
		snakeParts.add(0, head);
		if(head.x == apple.x && head.y == apple.y) {
			apple.isAlive = false;
			return;
		}
		removeTail();
	}

	public void setDirection(Direction direction) {
		if((this.direction == Direction.RIGHT && direction != Direction.LEFT)
				|| (this.direction == Direction.LEFT && direction != Direction.RIGHT)
				|| (this.direction == Direction.DOWN && direction != Direction.UP)
				|| (this.direction == Direction.UP && direction != Direction.DOWN)) {
			if((this.direction == Direction.LEFT && snakeParts.get(0).x != snakeParts.get(1).x)
				||( this.direction == Direction.RIGHT && snakeParts.get(0).x != snakeParts.get(1).x)
			|| (this.direction == Direction.UP && snakeParts.get(0).y != snakeParts.get(1).y)
			|| (this.direction == Direction.DOWN && snakeParts.get(0).y != snakeParts.get(1).y)){
				this.direction = direction;
			}
		}
	}

	public void draw(Game game) {
		for(int i = 0; i < snakeParts.size(); i++) {
			GameObject obj = snakeParts.get(i);
			if(i == 0) {
				if(isAlive) {
					game.setCellValueEx(obj.x, obj.y, Color.NONE, HEAD_SIGN, Color.GREEN, 75);
				} else {
					game.setCellValueEx(obj.x, obj.y, Color.NONE, HEAD_SIGN, Color.RED, 75);
				}
			} else {
				if(isAlive) {
					game.setCellValueEx(obj.x, obj.y, Color.NONE, BODY_SIGN, Color.GREEN, 75);
				} else {
					game.setCellValueEx(obj.x, obj.y, Color.NONE, BODY_SIGN, Color.RED, 75);
				}
			}
		}
	}
}
