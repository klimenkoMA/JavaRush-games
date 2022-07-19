package com.javarush.games.moonlander;

import com.javarush.engine.cell.*;

import java.util.List;

public class RocketFire extends GameObject{

	private List<int[][]> frames;
	private int frameIndex;
	private boolean isVisible;

	public RocketFire(List<int[][]> frameList) {
		super(0, 0, frameList.get(0));
		frames = frameList;
		frameIndex = 0;
		isVisible = false;
	}

	public void hide(){
		isVisible = false;
	}

	public void show(){
		isVisible = true;
	}

	private void nextFrame(){

		frameIndex += 1;
		if(frameIndex >= frames.size()){
			frameIndex = 0;
		}
		matrix = frames.get(frameIndex);
	}

	@Override
	public void draw(Game game) {
		if(isVisible){
			nextFrame();
			super.draw(game);
		}
	}
}
